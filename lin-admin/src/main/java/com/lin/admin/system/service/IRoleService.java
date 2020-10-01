package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.system.entity.Role;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface IRoleService extends IService<Role> {

    Role get(Long id);

    List<Role> list(Map<String, Object> params);

    int saveRole(Role role);

    int update(Role role);

    int remove(Long id);

    List<Role> list(Long userId);

    int batchremove(Long[] ids);

    List<Role> list();

    int count(Map<String, Object> map);

    /**
     * 查询用户角色关联的业务类型
     *
     * @param userId
     * @return
     */
    List<String> selectRoleBuType(Long userId);
}
