package unity.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unity.config.WorkIndex;
import unity.dao.UserMapper;
import unity.pojo.Assess;
import unity.pojo.Work;
import unity.service.AssessServiceImpl;
import unity.service.WorkServiceImpl;
import unity.service.WorkvoServiceImpl;
import unity.vo.Workvo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
*   前台有关实验的controller
 */
@Controller
public class WorkController {

    @Value("${image.address}")
    private String imageAddress;//图片保存基地址

    @Value("${work.address}")
    private String workAddress;//实验保存基地址

    //分页时每页显示数目
    @Value("${myindex.artitleSum}")
    private Integer pagesum;
    //分页按钮数目，要与前端html对应
    @Value("${pagebuttonsum}")
    private  Integer pagebuttonsum;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkServiceImpl workServiceImpl;

    @Autowired
    private WorkvoServiceImpl workvoServiceImpl;

    @Autowired
    private AssessServiceImpl assessServiceImpl;

    @Autowired
    private WorkIndex workIndex;

    @Autowired
    MessageSource messageSource;


    /**
     * 通过分类获取实验
     * @param worktype
     * @param page   当前页码
     * @param model
     * @return
     */
    @RequestMapping(value = "/g/type/{worktype}",method = RequestMethod.GET)
    public String showWorksByType(@PathVariable Integer worktype,
                                  @RequestParam(value = "page",defaultValue = "1")int page,
                                  Model model){

        //下面pagehelper必须先打开，再进行查询操作，才能成功分页
        PageHelper.startPage(page,pagesum,"id desc");
        List<Work> works=workServiceImpl.getWorksByType(worktype);
        PageInfo<Work> data=new PageInfo<>(works);
        List<Work> worklist=data.getList();

        Long count=data.getTotal();
        long allpages=count%pagesum==0?(count/pagesum):(count/pagesum+1);

        List<Workvo> workresults=new ArrayList<>();
        for(Work work:worklist){
            Workvo workvo= workvoServiceImpl.pojoTovo(work, WorkvoServiceImpl.WORKSRESULT_MODE);
            workresults.add(workvo);
        }
        Long startpage= workvoServiceImpl.pageprocess(page,pagebuttonsum);

        Locale locale= LocaleContextHolder.getLocale();
        String queryword="";
        switch (worktype){
            case 1:
                queryword=messageSource.getMessage("index.Chemistry",null,locale);
                break;
            case 2:
                queryword=messageSource.getMessage("index.Physics",null,locale);
                break;
            case 3:
                queryword=messageSource.getMessage("index.Biology",null,locale);
                break;
        }
        model.addAttribute("queryword",queryword);
        model.addAttribute("workresults",workresults);
        model.addAttribute("allpages",allpages);
        model.addAttribute("currentpage",page);
        model.addAttribute("startpage",startpage);
        return "worksresult";

    }

    /**
     * 获取实验详情
     * @param workid
     * @param model
     * @return
     */
    @RequestMapping(value="/g/id/{workid}",method = RequestMethod.GET)
    public String showWorkDetail(@PathVariable("workid") Integer workid,
                                 Model model){
        Work work=workServiceImpl.getWork(workid);
        Workvo workvo=workvoServiceImpl.pojoTovo(work,WorkvoServiceImpl.WORKDETAIL_MODE);
        List<Assess> assesslist=assessServiceImpl.getAssessByWorkId(workid);

        model.addAttribute("assesslist",assesslist);
        model.addAttribute("workvo",workvo);
        return "workdetail";
    }


    /**
     * 实验下载
     * @param response
     * @param fileaddress 实验保存的相对地址  /上传者id/实验名称
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/download",method = RequestMethod.POST)
    public void downloadWork(HttpServletResponse response,String fileaddress)throws Exception{
        String filepath=workAddress+fileaddress;
        String temp=fileaddress.substring(fileaddress.lastIndexOf('/')+1);
        String fileName= URLEncoder.encode(temp,"UTF-8");

        File file = new File(filepath);
        if (file.exists()) {
            response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName+";filename*=utf-8''"+fileName);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;

            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        }
    }


    /**
     * 通过名称和关键字模糊查询实验，可以post也可以get方法
     * @param queryword
     * @param page
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/g/search")
    public String showWorksBySearch(@RequestParam("queryword") String queryword,
                                    @RequestParam(value = "page",defaultValue = "1")int page,
                                    Model model)throws Exception{

        //todo 简单分页
        List<Work> list=workIndex.searchWork(queryword);

        int count=list.size();
        long allpages=count%pagesum==0?(count/pagesum):(count/pagesum+1);

        int fromindex=(page - 1) * pagesum;
        int toIndex = count>= page * pagesum ? page* pagesum : count;

        List<Work> worklist=list.subList(fromindex,toIndex);

        List<Workvo> workresults=new ArrayList<>();
        for(Work work:worklist){
            Workvo workvo= workvoServiceImpl.pojoTovo(work, WorkvoServiceImpl.WORKSRESULT_MODE);
            workresults.add(workvo);
        }
        Long startpage= workvoServiceImpl.pageprocess(page,pagebuttonsum);

        model.addAttribute("workresults",workresults);
        model.addAttribute("allpages",allpages);
        model.addAttribute("currentpage",page);
        model.addAttribute("startpage",startpage);
        model.addAttribute("queryword",queryword);
        return "worksresult";
    }

    /**
     * 查找所有免费实验
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "forfree")
    public String getallfree(@RequestParam(value = "page",defaultValue = "1")int page,
                             Model model){
        //下面pagehelper必须先打开，再进行查询操作，才能成功分页
        PageHelper.startPage(page,pagesum,"id desc");
        List<Work> works=workServiceImpl.getallfree();
        PageInfo<Work> data=new PageInfo<>(works);
        List<Work> worklist=data.getList();

        Long count=data.getTotal();
        long allpages=count%pagesum==0?(count/pagesum):(count/pagesum+1);

        List<Workvo> workresults=new ArrayList<>();
        for(Work work:worklist){
            Workvo workvo= workvoServiceImpl.pojoTovo(work, WorkvoServiceImpl.WORKSRESULT_MODE);
            workresults.add(workvo);
        }
        Long startpage= workvoServiceImpl.pageprocess(page,pagebuttonsum);
        model.addAttribute("workresults",workresults);
        model.addAttribute("allpages",allpages);
        model.addAttribute("currentpage",page);
        model.addAttribute("startpage",startpage);
        model.addAttribute("type","forfree");
        return "worksresult";
    }

}
