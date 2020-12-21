package com.codelnn.emms.shiro;

import com.codelnn.emms.entity.UserEntity;
import com.codelnn.emms.service.UserService;
import com.codelnn.emms.utils.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 13:47
 **/
@Service
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException(" token错误，请重新登入！");
        }
        UserEntity userByName = userService.findUserByName(username);
        if (userByName == null){
            throw new AccountException("账号不存在!");
        }
        if (JwtUtil.isExpire(token)){
            throw new AuthenticationException(" token过期，请重新登入！");
        }
        if (!JwtUtil.verify(token,username,userByName.getPassword())){
            throw new CredentialsException("密码错误!");
        }
        return new SimpleAuthenticationInfo(userByName,token,getName());
    }
}
