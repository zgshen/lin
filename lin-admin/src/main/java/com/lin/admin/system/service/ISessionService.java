package com.lin.admin.system.service;

import com.lin.admin.system.entity.User;
import com.lin.admin.system.model.UserOnline;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ISessionService {
    List<UserOnline> list();

    List<User> listOnlineUser();

    Collection<Session> sessionList();

    boolean forceLogout(String sessionId);

    Session checkSession(String token);

}
