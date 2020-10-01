package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.common.model.Tree;
import com.lin.admin.system.entity.Dept;
import com.lin.admin.system.entity.User;
import com.lin.admin.system.model.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface IUserService extends IService<User> {
    User get(Long id);

    List<User> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int saveUser(User user);

    int update(User user);

    int updateUser(User user);

    int batchremove(Long[] userIds);

    boolean exit(Map<String, Object> params);

    Set<String> listRoles(Long userId);

    int resetPwd(UserVO userVO, User userDO);

    int adminResetPwd(UserVO userVO) throws Exception;

    Tree<Dept> getTree();

    /**
     * 更新个人信息
     *
     * @param userDO
     * @return
     */
    int updatePersonal(User userDO);

    /**
     * 更新个人图片
     *
     * @param file        图片
     * @param avatar_data 裁剪信息
     * @param userId      用户ID
     * @throws Exception
     */
    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

    User getByUsername(String username);

}
