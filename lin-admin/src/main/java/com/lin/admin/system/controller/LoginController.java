package com.lin.admin.system.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.lin.admin.common.annotation.Log;
import com.lin.admin.common.controller.BaseController;
import com.lin.admin.common.emuns.ResultEnum;
import com.lin.admin.common.model.Result;
import com.lin.admin.common.utils.*;
import com.lin.admin.system.entity.PasswordStrategy;
import com.lin.admin.system.entity.User;
import com.lin.admin.system.service.IPasswordStrategyService;
import com.lin.admin.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Api(value = "登录接口", tags = {"登录接口"})
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPasswordStrategyService passwordStrategyService;

    @Log("用户登录")
    @PostMapping("/user/login")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "captcha", value = "验证码", dataType = "string", paramType = "query", required = true)
    })
    Result login(String username, String password, String captcha, HttpServletRequest request) {
        String newPassword;
        String newUsername;

        User userDO = userService.getByUsername(username);
        if (userDO == null) {
            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(),"用户或密码错误");
        }
        // 用户锁定时的时间
        Date lockTime = userDO.getLockTime();

        PasswordStrategy passwordStrategy = passwordStrategyService.getCurrentSystemPasswordStrategy(null);
        // 最大无效登录尝试次数
        Integer maxRetryTime = passwordStrategy.getRetryTime();
        // 用户密码有效期(单位:天):0-永不过期
        Integer expirationDate = passwordStrategy.getExpirationDate();
        // 锁定有效期间(单位:分):0-始终(必须由管理员重置)
        Integer lockingTime = passwordStrategy.getLockingTime();

        // 锁定验证
        if (1 == userDO.getIsLock()) {
            // 用户锁定
            if (0 == lockingTime) {
                // 必须由管理员解锁
                return ResultUtil.renderFail(ResultEnum.FAIL.getCode(),"用户已锁定,请联系管理员解锁");
            } else {
                DateTime dateTime = DateUtil.offsetMinute(lockTime, lockingTime);
                int i = dateTime.compareTo(DateTime.now());
                if (i < 0) {
                    // 超过锁定时间,进行解锁
                    userDO.setIsLock(0);
                    userDO.setRetryTime(0);
                    userService.updateUser(userDO);
                } else {
                    long between = DateUtil.between(lockTime, new Date(), DateUnit.MINUTE);
                    long lessTime = lockingTime - between;
                    return ResultUtil.renderFail(ResultEnum.FAIL.getCode(),"用户已锁定,请在 " + lessTime + " 分钟后重试");
                }
            }
        }

        // 有效期验证
        if (0 != expirationDate) {
            Date pwdUpdateTime = userDO.getPwdUpdateTime();
            DateTime dateTime = DateUtil.offsetDay(pwdUpdateTime, expirationDate);
            int i = dateTime.compareTo(DateTime.now());
            if (i < 0) {
                return ResultUtil.renderFail(ResultEnum.FAIL.getCode(),"密码已过期,请修改密码");
            }
        }

        newPassword = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, newPassword);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);//com.lin.admin.system.shiro.UserRealm校验实现
            Session session = subject.getSession();
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("token", session.getId());

            // 登录成功,清除登录失败次数
            userDO.setRetryTime(0);
            userService.updateUser(userDO);
            return ResultUtil.renderSuccess(data);
        } catch (AuthenticationException e) {
            // 登录失败,登录失败次数+1
            Integer retryTime = userDO.getRetryTime() + 1;
            if (retryTime > maxRetryTime - 1) {
                // 超过最大登录失败次数,锁定账号
                userDO.setRetryTime(retryTime);
                userDO.setIsLock(1);
                userDO.setLockTime(new Date());
                userService.updateUser(userDO);
            } else {
                userDO.setRetryTime(retryTime);
                userService.updateUser(userDO);
            }

            return ResultUtil.renderFail(ResultEnum.PARAMS_ERROR.getCode(),"用户或密码错误");
        }
    }


    @ApiOperation(value = "用户登出接口", produces = "application/json")
    @GetMapping("/user/logout")
    @ResponseBody
    R ajaxLogout() {
        ShiroUtils.logout();
        return R.ok();
    }

    @ApiOperation(value = "获取签名", produces = "application/json")
    @PostMapping("/getSign")
    @ResponseBody
    Result getSign(HttpServletRequest request) {

        HashMap<String, Object> data = new HashMap<>(1);
        String key = "salt-" + IPUtils.getIpAddr(request);
        // 随机生成密钥
        String sign = AesUtil.getKey();
        /*try {
            redisService.set(key, JacksonUtil.toJSon(sign));
        } catch (Exception e) {
            log.error(" redis 异常", e);
            return ResultUtil.renderFail(ResultEnum.UNKNOWN_ERROR);
        }*/
        data.put("sign", sign);
        return ResultUtil.renderSuccess(data);
    }

    @ApiOperation(value = "生成加密登录信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true)
    })
    @PostMapping("/encryptAccount")
    @ResponseBody
    Result encryptAccount(String username, String password, HttpServletRequest request) {

        Result result = getSign(request);
        HashMap<String, Object> data = (HashMap<String, Object>) result.getData();
        String sign = (String) data.get("sign");

        String newUsername = AesUtil.enCode(username, sign);
        String newPassword = AesUtil.enCode(password, sign);
        data.put("username", newUsername);
        data.put("password", newPassword);

        return ResultUtil.renderSuccess(data);
    }
}
