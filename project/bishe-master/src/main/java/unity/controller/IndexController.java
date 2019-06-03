package unity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import unity.common.Result;
import unity.dao.CasesMapper;
import unity.dao.ComplainMapper;
import unity.dao.NewsMapper;
import unity.pojo.Cases;
import unity.pojo.Complain;
import unity.pojo.News;
import unity.pojo.User;
import unity.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页controller
 */
@Controller
public class IndexController {

    @Autowired
    CasesMapper casesMapper;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ComplainMapper complainMapper;

    /**
     * 展示首页
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String showindex(Model model){
        List<News> newslist=(ArrayList)newsMapper.selectAll();
        List<Cases> caselist=(ArrayList)casesMapper.selectAll();
        if(newslist.size()<3){
            model.addAttribute("newsresult",newslist);
        }else{
            List<News> newsresult= newslist.subList(0,3);
            model.addAttribute("newsresult",newsresult);
        }
        if(caselist.size()<4){
            model.addAttribute("caseresult",caselist);
        }else{
            List<Cases> caseresult= caselist.subList(0,4);
            model.addAttribute("caseresult",caseresult);
        }
        return "index";
    }


    /**
     * 后台登入
     * @param request
     * @return
     */
    @RequestMapping("/backstage")
    public String gotobackstage(HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("currentUser");
        //根据用户类型，判断是root管理员还是普通实验作者
        if(user.getUsertype()==3){
            return "root/rootindex";
        }else{
            return "admin/adminindex";
        }
    }


    /**
     * 投诉功能
     * @param manid  投诉人
     * @param content 投诉内容
     * @return
     */
    @RequestMapping(value = "/complain",method = RequestMethod.GET)
    @ResponseBody
    public Result complain(@RequestParam("manid")Integer manid,
                           @RequestParam("content")String content){
        Result result=new Result();
        User user=userService.getUser(manid);
        Complain complain=new Complain();

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");

        complain.setComplaintime(simpleDateFormat.format(date).toString());
        complain.setManid(manid);
        complain.setManname(user.getUsername());
        complain.setContent(content);

        Integer r=complainMapper.insert(complain);

        //创建失败
        if(r!=1){
            result.setMsg("400");
        }
        return result;
    }

}
