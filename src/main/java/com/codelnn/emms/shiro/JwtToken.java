package com.codelnn.emms.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 13:36
 **/
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
