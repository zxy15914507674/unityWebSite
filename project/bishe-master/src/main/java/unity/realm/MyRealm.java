package unity.realm;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import unity.pojo.User;
import unity.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


/**
 * @Author xp
 * @Description 自定义realm
 * @Date 2017/4/20 16:45
 */
public class MyRealm extends AuthorizingRealm{

    @Autowired
    private UserServiceImpl userService;

    private User user;



    /**
     * 为当前登陆的用户授予角色和权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //这里只着重于角色，所以只有角色而不需要权限的二级细分
        String email=(String)SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        if(user==null){
            user=userService.getUserByEmail(email);
        }
        if(user.getUsertype()!=0){  //有可能是管理员也有可能是作者
            Set<String> set=new HashSet<>();
            set.add("admin");
            simpleAuthorizationInfo.setRoles(set);
        }
        return simpleAuthorizationInfo;
    }



    /**
     * 身份认证,使用邮箱地址
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)  throws AuthenticationException{
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;

        //获取用户邮箱地址及密码
        String email = (String) authenticationToken.getPrincipal();
        String password = new String(token.getPassword());

        //从数据库查询用户信息
        user = userService.getUserByEmail(email);

        if (user != null&&user.getPassword().equals(password)) {
            SecurityUtils.getSubject().getSession().setAttribute("currentUser", user);//把当前用户存到session中
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(
                    email, password, "MyRealm");
            return authcInfo;
        } else {
            //返回空会使得controller处报异常，说明没有找到账号信息
            return null;
        }
    }

}
