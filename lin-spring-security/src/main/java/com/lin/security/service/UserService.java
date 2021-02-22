package com.lin.security.service;

import com.lin.security.entity.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 模拟数据库操作
 */
@Service
public class UserService {

    public CustomUser getUserByUsername(String username) {
        /**
         * 数据库查询这里操作
         */
        return new CustomUser(1, "sc-admin", new BCryptPasswordEncoder().encode("123456"), getGrants("ROLE_admin"));
    }

    /**
     * 两种授权方法，即角色授权和权限授权，对应使用的代码是hasRole和hasAuthority，而这两种方式在设置时也有不同，下面介绍一下：
     * 角色授权：授权代码需要加ROLE_前缀，controller上注解使用时不要加前缀
     * 权限授权：设置和使用时，名称保持一至即可
     *
     * 1. commaSeparatedStringToAuthorityList放入角色时需要加前缀ROLE_，而在controller使用时不需要加ROLE_前缀
     * 2. 放入的是权限时，不能加ROLE_前缀，hasAuthority与放入的权限名称对应即可
     * @param role
     * @return
     */
    private Collection<GrantedAuthority> getGrants(String role) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }

}
