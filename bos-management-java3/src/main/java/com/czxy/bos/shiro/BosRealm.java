package com.czxy.bos.shiro;

import com.czxy.bos.domain.system.Permission;
import com.czxy.bos.domain.system.Role;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.service.system.PermissionService;
import com.czxy.bos.service.system.RoleService;
import com.czxy.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/** 自定义realm
 *
 */
public class BosRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Override
    public String getName() {
        return "BosRealm";      //习惯命名
    }

    /**
     * 认证方法 ： 用户登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证。。。。");
        //1 获得登录用户名
        String username = (String)token.getPrincipal();       //身份信息（用户名）
        //token.getCredentials();     //凭证信息（密码）

        //2 使用用户名查询用户，shiro规定：如果return null 表示用户不存在。
        User user = userService.findUserByUsername( username );
        if(user == null){
            return null;  //表示用户不存在，shiro将自动【抛异常】
        }

        //3 将查询到的用户密码，交予shiro，让shiro进行用户填写登录密码进行匹配
        // 匹配成功(密码正常) 程序正常执行。匹配失败(密码不对)shiro将自动【抛异常】
        return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
    }

    /**
     * 授权方法 ：登录是否具有某一些权利
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权。。。。");

        //1 准备返回对象
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //2 获得当前登录用户信息
        //User user = (User)principals.getPrimaryPrincipal();
        User user = (User)SecurityUtils.getSubject().getPrincipal();

        //3 通过用户查询角色，并添加到返回对象中（交予shiro）
        List<Role> roleList = this.roleService.findByUser( user );
        for(Role role : roleList){
            authorizationInfo.addRole( role.getKeyword() );
        }

        //4 通过用户查询角色，并添加到返回对象中（交予shiro）
        List<Permission> permsList = this.permissionService.findByUser( user );
        for(Permission perm : permsList ){
            authorizationInfo.addStringPermission( perm.getKeyword() );
        }

        return authorizationInfo;
    }
}
