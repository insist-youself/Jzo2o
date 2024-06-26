package com.jzo2o.mvc.handler;

import com.jzo2o.common.handler.UserInfoHandler;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.mvc.utils.UserContext;
import org.springframework.stereotype.Component;

@Component
public class UserInfoHandlerImpl implements UserInfoHandler {
    @Override
    public CurrentUserInfo currentUserInfo() {
        return UserContext.currentUser();
    }
}
