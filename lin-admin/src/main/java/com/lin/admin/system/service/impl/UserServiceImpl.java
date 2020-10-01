package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.config.AdminConfig;
import com.lin.admin.common.entity.File;
import com.lin.admin.common.model.Tree;
import com.lin.admin.common.service.IFileService;
import com.lin.admin.common.utils.*;
import com.lin.admin.system.entity.*;
import com.lin.admin.system.mapper.DeptMapper;
import com.lin.admin.system.mapper.UserMapper;
import com.lin.admin.system.mapper.UserRoleMapper;
import com.lin.admin.system.model.UserVO;
import com.lin.admin.system.service.IPasswordStrategyService;
import com.lin.admin.system.service.IUserPwdHistoryService;
import com.lin.admin.system.service.IUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(IUserService.class);

    @Autowired
    private UserRoleMapper userRoleDao;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private IFileService sysFileService;
    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserPwdHistoryService userPwdHistoryService;
    @Autowired
    private IPasswordStrategyService passwordStrategyService;

    @Override
    public User get(Long id) {
        User user = userMapper.selectById(id);
        if (null == user) {
            return user;
        }
        List<Long> roleIds = userRoleDao.listRoleId(id);
        if (user.getDeptId() != null) {
            Dept deptDO = deptMapper.get(user.getDeptId());
            if (deptDO != null) {
                user.setDeptName(deptMapper.get(user.getDeptId()).getName());
            }
        }
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        List<User> users = userMapper.list(map);
        for (User user : users) {
            Dept deptDO = deptMapper.get(user.getDeptId());
            if (deptDO != null) {
                user.setDeptName(deptMapper.get(user.getDeptId()).getName());
            }
            user.setPassword("******");
        }

        return users;
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Transactional
    @Override
    public int saveUser(User user) {
        Date now = new Date();
        user.setGmtCreate(now);
        user.setGmtModified(now);
        int count = userMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleDao.removeByUserId(userId);
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleDao.batchSave(list);
        }
        return count;
    }

    @Override
    public int update(User user) {
        int r = userMapper.update(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        if (roles == null || roles.size() <= 0) {
            return r;
        }
        userRoleDao.removeByUserId(userId);
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleDao.batchSave(list);
        }
        return r;
    }

    @Override
    public int updateUser(User user) {

        return userMapper.update(user);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userMapper.list(params).size() > 0;
        return exit;
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVO, User userDO) {
        if (userVO.getUserDO().getUserId().equals(userDO.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
                String newPassword = MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew());
                this.savePwdHistory(userDO, newPassword);

                userDO.setPassword(newPassword);
                userDO.setRetryTime(0);
                userDO.setIsLock(0);
                userDO.setPwdUpdateTime(new Date());
                return userMapper.update(userDO);
            } else {
                throw new RuntimeException("输入的旧密码有误！");
            }
        } else {
            throw new RuntimeException("你修改的不是你登录的账号！");
        }
    }

    @Override
    public int adminResetPwd(UserVO userVO) throws Exception {
        User userDO = get(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        String newPassword = MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew());
        this.savePwdHistory(userDO, newPassword);

        userDO.setPassword(newPassword);
        userDO.setRetryTime(0);
        userDO.setIsLock(0);
        userDO.setPwdUpdateTime(new Date());
        return userMapper.update(userDO);
    }

    /**
     * 保存用户修改密码历史
     *
     * @param userDO
     * @param newPassword
     */
    private void savePwdHistory(User userDO, String newPassword) {
        PasswordStrategy passwordStrategy = passwordStrategyService.getCurrentSystemPasswordStrategy(null);
        Integer passwordHistory = passwordStrategy.getPasswordHistory();
        if (0 == passwordHistory) {
            userPwdHistoryService.delete(userDO.getUserId());
        } else {
            // 保存密码修改记录
            UserPwdHistory userPwdHistory = new UserPwdHistory();
            userPwdHistory.setUserId(userDO.getUserId());
            userPwdHistory.setPassword(newPassword);
            userPwdHistory.setCreateTime(new Date());
            List<UserPwdHistory> userPwdHistories = userPwdHistoryService.getList(userDO.getUserId());
            if (userPwdHistories != null && userPwdHistories.size() >= passwordHistory) {
                // 密码修改历史大于或等于限制数,去除多余的记录,并保存新记录
                List<UserPwdHistory> removeList = userPwdHistories.subList(passwordHistory - 1, userPwdHistories.size());
                for (UserPwdHistory pwdHistory : removeList) {
                    userPwdHistoryService.removeById(pwdHistory.getId());
                }
            }
            userPwdHistoryService.save(userPwdHistory);
        }
    }

    @Transactional
    @Override
    public int batchremove(Long[] userIds) {
        int count = userMapper.batchRemove(userIds);
        userRoleDao.batchRemoveByUserId(userIds);
        return count;
    }

    @Override
    public Tree<Dept> getTree() {
        List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
        List<Dept> depts = deptMapper.list(new HashMap<String, Object>(16));
        Long[] pDepts = deptMapper.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (Dept dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<User> users = userMapper.list(new HashMap<String, Object>(16));
        for (User user : users) {
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Dept> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public int updatePersonal(User userDO) {
        return userMapper.update(userDO);
    }

    @Override
    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        File sysFile = new File(FileType.fileType(fileName), "/files/" + fileName, new Date());

        if (StringUtils.isNotBlank(avatar_data)) {
            //获取图片后缀
            String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
            String[] str = avatar_data.split(",");
            //获取截取的x坐标
            int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
            //获取截取的y坐标
            int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
            //获取截取的高度
            int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
            //获取截取的宽度
            int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
            //获取旋转的角度
            int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
            try {
                BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
                BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                boolean flag = ImageIO.write(rotateImage, prefix, out);
                //转换后存入数据库
                byte[] b = out.toByteArray();
                FileUtil.uploadFile(b, adminConfig.getUploadPath() + "/files/", fileName);
            } catch (Exception e) {
                throw new Exception("图片裁剪错误！！");
            }
        } else {
            FileUtil.uploadFile(file.getBytes(), adminConfig.getUploadPath() + "/files/", fileName);
        }
        Map<String, Object> result = new HashMap<>(16);
        if (sysFileService.save(sysFile) > 0) {
            User userDO = new User();
            userDO.setUserId(userId);
            userDO.setPicId(sysFile.getId());
            if (userMapper.update(userDO) > 0) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

}
