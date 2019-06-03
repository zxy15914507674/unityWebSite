package unity.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unity.common.Result;
import unity.dao.CasesMapper;
import unity.dao.NewsMapper;
import unity.pojo.Cases;
import unity.pojo.News;
import unity.pojo.User;
import unity.service.UserServiceImpl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 网站管理Controller
 */
@Controller
public class RootController {

    @Autowired
    CasesMapper casesMapper;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    UserServiceImpl userService;

    @Value("${index.image}")
    private String imageAddress;

    @RequestMapping(value = "/root/getallcases",method = RequestMethod.GET)
    @ResponseBody
    public Result showallcases(@RequestParam(value = "page",defaultValue = "1")int page,
                                @RequestParam(value = "limit",defaultValue = "5") int limit){
        PageHelper.startPage(page,limit,"id asc");
        Result result=new Result();
        ArrayList<Cases> list=(ArrayList) casesMapper.selectAll();
        PageInfo<Cases> datameta = new PageInfo<>(list);
        List<Cases> works=datameta.getList();
        result.setCount((int)datameta.getTotal());
        result.setData(works);
        result.setCode(0);
        return result;
    }

    @RequestMapping(value="/root/getallnews",method = RequestMethod.GET)
    @ResponseBody
    public Result showallnews(@RequestParam(value = "page",defaultValue = "1")int page,
                               @RequestParam(value = "limit",defaultValue = "5") int limit){
        PageHelper.startPage(page,limit,"id asc");
        Result result=new Result();
        ArrayList<News> list=(ArrayList) newsMapper.selectAll();
        PageInfo<News> datameta = new PageInfo<>(list);
        List<News> works=datameta.getList();
        result.setCount((int)datameta.getTotal());
        result.setData(works);
        result.setCode(0);
        return result;
    }

    /**
     * 根据名字打开特定的Tab
     * @param name
     * @return
     */
    @RequestMapping(value = "/root/gethtml",method = RequestMethod.GET)
    public String getHtml(@RequestParam(value="pageName")String name,
                          @RequestParam(value = "param",defaultValue = "null")String param,
                          Model model){
        if(name.equals("someonework")){
            model.addAttribute("workmanid",Integer.parseInt(param));
        }
        return "root/"+name;
    }

    /**
     * 创建一个公告
     * @param news
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/root/addnews",method = RequestMethod.POST)
    @ResponseBody
    public Result addnews(@RequestBody News news) throws Exception{
        Result result=new Result();
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        news.setCtime(format.format(date));
        if(newsMapper.insert(news)==1){
            return result;
        }else{
            result.setCode(400); //失败
        }
        return result;
    }

    /**
     * 删除一个公告
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/root/delnews",method = RequestMethod.GET)
    @ResponseBody
    public Result delnews(Integer id ) throws Exception{
        Result result=new Result();
        if(newsMapper.deleteByPrimaryKey(id)==1){
            return result;
        }else{
            result.setCode(400); //失败
        }
        return result;
    }

    /**
     * 创建一个使用案例
     * @param cases
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/root/addcase",method = RequestMethod.POST)
    @ResponseBody
    public Result addcase(Cases cases,@RequestParam("files") MultipartFile image) throws Exception{
        Result result=new Result();
        String imagename=UUID.randomUUID().toString().substring(0,20);

        //获取图片
        String filetype=image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        File workfile=new File(imageAddress+File.separator+imagename+filetype);

        image.transferTo(workfile);

        cases.setImage(imagename+filetype);

        if(casesMapper.insert(cases)==1){
            return result;
        }else{
            result.setCode(400); //上传失败
        }
        return result;
    }

    /**
     * 删除一个使用案例
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/root/delcase",method = RequestMethod.GET)
    @ResponseBody
    public Result delcase( Integer id ) throws Exception{
        Result result=new Result();
        if(casesMapper.deleteByPrimaryKey(id)==1){
            return result;
        }else{
            result.setCode(400); //失败
        }
        return result;
    }

    /**
     * 查看所有注册用户
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/root/getallusers",method = RequestMethod.GET)
    @ResponseBody
    public Result getalluser(@RequestParam(value = "page",defaultValue = "1")int page,
                             @RequestParam(value = "limit",defaultValue = "5") int limit) throws Exception{
        PageHelper.startPage(page,limit,"id asc");
        Result result=new Result();
        ArrayList<User> list=(ArrayList) userService.getallUsers();
        PageInfo<User> datameta = new PageInfo<>(list);
        List<User> users=datameta.getList();
        result.setCount((int)datameta.getTotal());
        result.setData(users);
        result.setCode(0);
        return result;
    }

}
