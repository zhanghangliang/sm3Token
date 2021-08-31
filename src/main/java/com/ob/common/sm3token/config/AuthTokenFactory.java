package com.ob.common.sm3token.config;

import com.ob.common.sm3token.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.logging.Logger;

@Configuration
public class AuthTokenFactory {

    private static final Logger LOG = Logger.getLogger(AuthTokenFactory.class.getName());

    public String authToken;

    @Autowired
    private TokenManager tokenManager;

    @Scheduled(fixedRate = 60 * 60 * 1000 * 24)
    public void init() {
        authToken = tokenManager.getToken();
    }

    public String getAuthToken() {
        if(authToken == null) {
            authToken = tokenManager.getToken();
        }
        return authToken;
    }
}
