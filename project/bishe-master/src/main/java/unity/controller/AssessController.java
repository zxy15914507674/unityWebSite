package unity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import unity.common.Result;
import unity.pojo.Assess;
import unity.pojo.User;
import unity.pojo.Work;
import unity.service.AssessServiceImpl;
import unity.service.UserServiceImpl;
import unity.service.WorkServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AssessController {


    @Autowired
    AssessServiceImpl assessService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    WorkServiceImpl workService;

    /**
     * 评论和打分
     * @param content
     * @param starvalue
     * @param valuerId
     * @param workId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/assess",method= RequestMethod.POST)
    public String createAssess(String content,Integer starvalue,
                               Integer valuerId,Integer workId)throws Exception{
        Assess assess=new Assess();
        User user=userService.getUser(valuerId);
        Work work=workService.getWork(workId);
        work.setId(work.getId()+starvalue);
        workService.updateWork(work,false);

        Date date=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String createtime=dateFormat.format(date);

        assess.setAssessername(user.getUsername());
        assess.setStar(starvalue);
        assess.setContent(content);
        assess.setValuerid(valuerId);
        assess.setAssessdate(createtime);
        assess.setWorkid(workId);

        int temp=assessService.createAssess(assess);
        return "redirect:/g/id/"+workId;
    }
}
