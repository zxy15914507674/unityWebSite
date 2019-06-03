package unity.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import unity.pojo.User;
import unity.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Controller
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    MessageSource messageSource;

    /**
     * 执行登录操作
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/tologin", method = RequestMethod.POST)
    public String login(User user, Model model){   //使用model不使用request的原因时IDEA能够解析
        //获取登录实体
        Subject subject = SecurityUtils.getSubject();

        //将输入的密码用MD5加密
        String md5string = new Md5Hash(user.getPassword(),"unity").toString();

        //创建获取用户密码登录token
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), md5string);
        try {
            //根据token登录 会调用MyRealm中的doGetAuthenticationInfo方法进行身份认证
            subject.login(token);
            return "redirect:/";
        } catch (AuthenticationException e) {
            //有异常说明登陆出现问题
            model.addAttribute("user", user);
            model.addAttribute("errorInfo", "用户名或密码错误");
            return "login";
        }
    }

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String tologin(){
        return "login";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String toregister(){
        return "register";
    }


    /**
     * 用户注册
     * @param user
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/toregister",method = RequestMethod.POST)
    public String register(User user, Model model, HttpServletRequest request){
        String email=user.getEmail();
        String password=new Md5Hash(user.getPassword(),"unity").toString();
        if(userService.getUserByEmail(email)!=null){
            Locale locale=LocaleContextHolder.getLocale();
            String errorInfo=messageSource.getMessage("register.isexist",null,locale);
            model.addAttribute("errorInfo",errorInfo);
            return  "register";
        }else{
            Date date=new Date();
            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
            String time=dateFormat.format(date);
            user.setRegistrationtime(time);
            user.setPassword(password);
            userService.addUser(user);
            request.getSession().setAttribute("currentUser",user);  //这个user是有主键返回的
        }
        return  "registersuccess";      //跳转到注册成功页面
    }
}
