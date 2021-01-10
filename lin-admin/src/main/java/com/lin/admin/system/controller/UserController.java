package com.lin.admin.system.controller;

import com.lin.admin.common.annotation.Log;
import com.lin.admin.common.controller.BaseController;
import com.lin.admin.common.emuns.ResultEnum;
import com.lin.admin.common.entity.File;
import com.lin.admin.common.model.Constant;
import com.lin.admin.common.model.Result;
import com.lin.admin.common.model.Tree;
import com.lin.admin.common.service.IDictService;
import com.lin.admin.common.service.IFileService;
import com.lin.admin.common.service.IRedisService;
import com.lin.admin.common.utils.*;
import com.lin.admin.system.entity.*;
import com.lin.admin.system.model.UserVO;
import com.lin.admin.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zhoubin
 */
@Api(value = "用户管理接口", tags = {"用户管理接口"})
@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IPasswordStrategyService passwordStrategyService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IRedisService redisService;

    @ApiOperation(value = "用户列表接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "分页参数-当前页,从 1 开始", dataType = "string", paramType = "query", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页参数-每页显示条数", dataType = "string", paramType = "query", defaultValue = "15", required = true),
            @ApiImplicitParam(name = "deptId", value = "部门 ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchText", value = "模糊查询条件:用户名/用户名/邮箱/手机号", dataType = "string", paramType = "query")
    })
    @GetMapping("/list")
    @ResponseBody
    @RequiresPermissions("sys:user:list")
    PageInfo list(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageHelper.startPage(Integer.parseInt(params.get("pageNum") + ""), Integer.parseInt(params.get("pageSize") + ""));
        List<Long> deptIdArr = new ArrayList<>();
        String deptId = (String) params.get("deptId");
        if (StringUtils.isNotBlank(deptId)) {
            Long parentId = Long.valueOf(deptId);
            if (parentId != null) {
                deptIdArr.add(parentId);
                getChildDeptIds(deptIdArr, parentId);// 找到关联的所有子部门
                params.put("deptIdArr", deptIdArr);
                params.remove("deptId");
            }
        }
        PageInfo<User> pageInfo = new PageInfo<>(userService.list(params));
        return pageInfo;
    }

    void getChildDeptIds(List<Long> deptIdArr, Long parentId) {
        List<Long> deptIds = deptService.getByParentId(parentId);
        if (deptIds != null && deptIds.size() > 0) {
            deptIdArr.addAll(deptIds);
            for (Long deptId : deptIds) {
                getChildDeptIds(deptIdArr, deptId);
            }
        }
    }

    @ApiOperation(value = "查询当前用户信息接口", produces = "application/json")
    @Log("查询当前用户信息")
    @RequestMapping("/queryCurrentUser")
    @ResponseBody
    @RequiresPermissions("sys:user:queryCurrentUser")
    public Result queryCurrentUser() {
        HashMap<String, Object> data = new HashMap<>(4);
        Long userId = getUserId();
        User user = userService.get(userId);
        if (user.getPicId() != null) {
            File fileDO = fileService.get(user.getPicId());
            if (fileDO != null) {
                user.setPicUrl(fileDO.getUrl());// 用户头像
            }
        }
        user.setPassword("******");
        List<Tree<Menu>> menus = menuService.listMenuTree(getUserId());
        data.put("user", user);
        data.put("menus", menus);
        return ResultUtil.renderSuccess(data);
    }

    @ApiOperation(value = "查询用户接口", produces = "application/json")
    @ApiImplicitParam(name = "userId", value = "用户 ID", dataType = "long", paramType = "query", required = true)
    @RequiresPermissions("sys:user:queryUser")
    @Log("查询用户")
    @PostMapping("/queryUser")
    @ResponseBody
    public Object queryUser(Long userId) {
        User userDO = userService.get(userId);
        userDO.setPassword("******");
        List<Role> roles = roleService.list(userId);

        Map data = new HashMap(4);
        data.put("user", userDO);
        data.put("roles", roles);
        return data;
    }

    @ApiOperation(value = "查询用户接口", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "用户 ID", dataType = "long", paramType = "query", required = true)
    @Log("查询用户接口")
    @RequiresPermissions("sys:user:edit")
    @PostMapping("/editUser")
    @ResponseBody()
    Object editUser(Long id) {
        User userDO = userService.get(id);
        List<Role> roles = roleService.list(id);

        HashMap<String, Object> data = new HashMap<>(4);
        data.put("user", userDO);
        data.put("roles", roles);
        return data;
    }

    @ApiOperation(value = "新增用户接口", produces = "application/json")
    @RequiresPermissions("sys:user:add")
    @Log("新增用户接口")
    @PostMapping("/save")
    @ResponseBody
    public Result save(User user, HttpServletRequest request) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultUtil.renderFail(1, "演示系统不允许修改,完整体验请部署程序");
        }

        // 校验用户名是否存在
        String userName = user.getUsername();
        HashMap<String, Object> queryMap = new HashMap<>(1);
        queryMap.put("username", userName);
        boolean exit = userService.exit(queryMap);
        if (exit) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "登录名已存在,请修改");
        }

        queryMap.clear();
        queryMap.put("name", user.getName());
        exit = userService.exit(queryMap);
        if (exit) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "用户名已存在,请修改");
        }

        String key = "salt-" + IPUtils.getIpAddr(request);
        String redisSign = null;
        try {
            redisSign = redisService.getEntity(key, String.class);
            if (null == redisSign) {
                // 签名不存在
                return ResultUtil.renderFail(ResultEnum.INVALID_SIGN);
            } else {
                // 签名存在,aes 反解密用户密码
                String newPassword = AesUtil.deCode(user.getPassword(), redisSign);
                // 校验成功,移除 redis 中的签名,每个签名只能使用一次
                user.setPassword(newPassword);
                redisService.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PasswordStrategy passwordStrategy = passwordStrategyService.getCurrentSystemPasswordStrategy(null);
        String result = PasswordValidation.passwordValidation(user, passwordStrategy);
        if (StringUtils.isNotBlank(result)) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), result);
        }
        Long userId = getUserId();
        user.setUserIdCreate(userId)
                .setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()))
                .setPwdUpdateTime(new Date())
                .setIsLock(0)
                .setRetryTime(0);
        if (userService.saveUser(user) > 0) {
            return ResultUtil.renderSuccess();
        }
        return ResultUtil.renderFail();
    }

    @ApiOperation(value = "管理员修改用户信息接口", produces = "application/json")
    @RequiresPermissions("sys:user:edit")
    @Log("管理员修改用户信息接口")
    @PostMapping("/update")
    @ResponseBody
    public Result update(User user) {
        Result result = validPersonInfo(user);
        if (0 != result.getCode()) {
            return result;
        }

        if (userService.update(user) > 0) {
            return ResultUtil.renderSuccess();
        }
        return ResultUtil.renderFail();
    }

    @ApiOperation(value = "用户修改本人信息接口", produces = "application/json")
    @Log("用户修改本人信息接口")
    @PostMapping("/updatePersonal")
    @ResponseBody
    @RequiresPermissions("sys:user:updatePersonal")
    public Result updatePersonal(User user) {
        Result result = validPersonInfo(user);
        if (0 != result.getCode()) {
            return result;
        }

        if (userService.updatePersonal(user) > 0) {
            return ResultUtil.renderSuccess();
        }
        return ResultUtil.renderFail();
    }

    /**
     * 验证用户信息
     *
     * @param user
     * @return
     */
    Result validPersonInfo(User user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultUtil.renderFail(1, "演示系统不允许修改,完整体验请部署程序");
        }

        User userDO = userService.get(user.getUserId());
        if (userDO == null) {
            return ResultUtil.renderFail(ResultEnum.NOT_DATA.getCode(), "用户不存在");
        }
        // 登录名不允许修改
        if (null != user.getUsername() && !user.getUsername().equals(userDO.getUsername())) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "登录名不允许修改");
        }

        int count;
        HashMap<String, Object> queryMap = new HashMap<>(2);
        if (StringUtils.isNotBlank(userDO.getEmail()) && !userDO.getEmail().equals(user.getEmail())) {
            // 校验邮箱是否存在
            String email = user.getEmail();
            queryMap.put("email", email);
            count = userService.count(queryMap);
            if (count > 0) {
                return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "邮箱已使用,请修改");
            }
        }
        if (StringUtils.isNotBlank(userDO.getMobile()) && !userDO.getMobile().equals(user.getMobile())) {
            // 校验手机号是否存在
            String mobile = user.getMobile();
            queryMap.clear();
            queryMap.put("mobile", mobile);
            count = userService.count(queryMap);
            if (count > 0) {
                return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "手机号已使用,请修改");
            }
        }
        return ResultUtil.renderSuccess();
    }

    @ApiOperation(value = "删除用户接口", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "用户 ID", required = true, paramType = "query", dataType = "long")
    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    public R remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.removeById(id)) {
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation(value = "批量删除用户", produces = "application/json")
    @ApiImplicitParam(name = "ids[]", value = "用户 ID 数组集", required = true, paramType = "query", allowMultiple = true, dataType = "long")
    @RequiresPermissions("sys:user:batchRemove")
    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R batchRemove(@RequestParam("ids[]") Long[] userIds) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        int r = userService.batchremove(userIds);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ApiIgnore
    @PostMapping("/exit")
    @ResponseBody
    @RequiresPermissions("sys:user:exit")
    public boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @ApiOperation(value = "免登录修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDO.username", value = "登录名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "pwdOld", value = "旧密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "pwdNew", value = "新密码", dataType = "string", paramType = "query", required = true)
    })
    @PostMapping("/resetPwdUnAuth")
    @ResponseBody
    public Result resetPwdUnAuth(@ApiIgnore UserVO userVO, HttpServletRequest request) {

        String username = null;
        String oldPassword = null;
        String newPassword = null;
        // 签名校验
        try {
            String key = "salt-" + IPUtils.getIpAddr(request);
            String redisSign = redisService.getEntity(key, String.class);
            if (null == redisSign) {
                // 签名不存在
                return ResultUtil.renderFail(ResultEnum.INVALID_SIGN);
            } else {
                // 签名存在,aes 反解密用户密码
                username = AesUtil.deCode(userVO.getUserDO().getUsername(), redisSign);
                newPassword = AesUtil.deCode(userVO.getPwdNew(), redisSign);
                oldPassword = AesUtil.deCode(userVO.getPwdOld(), redisSign);
                // 校验成功,移除 redis 中的签名,每个签名只能使用一次
                redisService.del(key);
                userVO.getUserDO().setUsername(username);
                userVO.setPwdNew(newPassword);
                userVO.setPwdOld(oldPassword);
                if (StringUtils.isBlank(newPassword)) {
                    return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "密码不能为空");
                }
            }
        } catch (Exception e) {
            logger.error("签名校验出错", e);
            return ResultUtil.renderFail(ResultEnum.UNKNOWN_ERROR);
        }
        if (StringUtils.isBlank(username)) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "登录名不能为空");
        }
        if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(oldPassword)) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "新旧密码不能为空");
        }
        if (Constant.DEMO_ACCOUNT.equals(username)) {
            return ResultUtil.renderFail(ResultEnum.FAIL.getCode(), "演示系统不允许修改,完整体验请部署程序");
        }
        User user = userService.getByUsername(username);
        if (user == null) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "用户不存在");
        }
        userVO.setUserDO(user);
        // 密码强度校验
        return getResult(userVO, user);
    }

    @ApiOperation(value = "用户修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDO.userId", value = "用户 ID", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "pwdOld", value = "旧密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "pwdNew", value = "新密码", dataType = "string", paramType = "query", required = true)
    })
    @Log("用户修改密码")
    @PostMapping("/resetPwd")
    @RequiresPermissions("sys:user:resetPwd")
    @ResponseBody
    public Result resetPwd(@ApiIgnore UserVO userVO, HttpServletRequest request) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultUtil.renderFail(ResultEnum.FAIL.getCode(), "演示系统不允许修改,完整体验请部署程序");
        }
        if (StringUtils.isBlank(userVO.getPwdNew()) || StringUtils.isBlank(userVO.getPwdOld())) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "新旧密码不能为空");
        }

        if (null == userVO.getUserDO() || null == userVO.getUserDO().getUserId()) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "用户不存在");
        }

        User user = userService.getById(userVO.getUserDO().getUserId());
        if (user == null) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), "用户不存在");
        }
        String key = "salt-" + IPUtils.getIpAddr(request);
        String redisSign = null;
        try {
            redisSign = redisService.getEntity(key, String.class);
            if (null == redisSign) {
                // 签名不存在
                return ResultUtil.renderFail(ResultEnum.INVALID_SIGN);
            } else {
                // 签名存在,aes 反解密用户密码
                String newPassword = AesUtil.deCode(userVO.getPwdNew(), redisSign);
                String oldPassword = AesUtil.deCode(userVO.getPwdOld(), redisSign);
                // 校验成功,移除 redis 中的签名,每个签名只能使用一次
                userVO.setPwdNew(newPassword);
                userVO.setPwdOld(oldPassword);
                redisService.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 密码强度校验
        return getResult(userVO, user);
    }

    private Result getResult(@ApiIgnore UserVO userVO, User user) {
        PasswordStrategy passwordStrategy = passwordStrategyService.getCurrentSystemPasswordStrategy(null);
        String result = PasswordValidation.passwordValidation(userVO, passwordStrategy);
        if (StringUtils.isNotBlank(result)) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(), result);
        }
        userService.resetPwd(userVO, user);
        return ResultUtil.renderSuccess();
    }

    @ApiOperation(value = "管理员提交更改用户密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDO.userId", value = "用户 ID", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "pwdNew", value = "新密码", dataType = "string", paramType = "query", required = true)
    })
    @RequiresPermissions("sys:user:adminResetPwd")
    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    public R adminResetPwd(@ApiIgnore UserVO userVO, HttpServletRequest request) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        try {
            String key = "salt-" + IPUtils.getIpAddr(request);
            String redisSign = null;
            try {
                redisSign = redisService.getEntity(key, String.class);
                if (null == redisSign) {
                    // 签名不存在
                    return R.error("密钥不存在，请重试！");
                } else {
                    // 签名存在,aes 反解密用户密码
                    String newPassword = AesUtil.deCode(userVO.getPwdNew(), redisSign);
                    String oldPassword = AesUtil.deCode(userVO.getPwdOld(), redisSign);
                    // 校验成功,移除 redis 中的签名,每个签名只能使用一次
                    userVO.setPwdNew(newPassword);
                    userVO.setPwdOld(oldPassword);
                    redisService.del(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 密码强度校验
            PasswordStrategy passwordStrategy = passwordStrategyService.getCurrentSystemPasswordStrategy(null);
            String result = PasswordValidation.passwordValidation(userVO, passwordStrategy);
            if (StringUtils.isNotBlank(result)) {
                return R.error(ResultEnum.PARAMS_ERROR.getCode(), result);
            }
            userService.adminResetPwd(userVO);
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

    @ApiOperation(value = "查询部门树", produces = "application/json")
    @GetMapping("/tree")
    @ResponseBody
    public Tree<Dept> tree() {
        return userService.getTree();
    }

    @ApiOperation(value = "更新图像接口", produces = "application/json")
    @ResponseBody
    @PostMapping("/uploadImg")
    @RequiresPermissions("sys:user:uploadImg")
    public R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        Map<String, Object> result;
        try {
            result = userService.updatePersonalImg(file, avatar_data, getUserId());
        } catch (Exception e) {
            return R.error("更新图像失败！");
        }

        if (result != null && result.size() > 0) {
            return R.ok(result);
        } else {
            return R.error("更新图像失败！");
        }
    }

    @ApiOperation(value = "用户解锁接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户 ID", dataType = "long", paramType = "query", required = true)
    })
    @RequiresPermissions("sys:user:unlock")
    @PostMapping("/unlock")
    @ResponseBody
    Result unlock(Long userId) {
        User userDO = userService.get(userId);
        userDO.setIsLock(0);
        userService.updateUser(userDO);
        return ResultUtil.renderSuccess();
    }
}
