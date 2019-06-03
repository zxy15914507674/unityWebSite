package unity.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unity.common.Result;
import unity.dao.UserMapper;
import unity.pojo.User;
import unity.pojo.Work;
import unity.service.WorkServiceImpl;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 后台管理的controller
 */
@Controller
public class AdminController {

    private static Logger logger= LoggerFactory.getLogger(AdminController.class);

    @Value("${image.address}")
    private String imageAddress;

    @Value("${work.address}")
    private String workAddress;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkServiceImpl workServiceImpl;


    //根据名字打开特定的Tab
    @RequestMapping(value = "/admin/gethtml",method = RequestMethod.GET)
    public String getHtml(Model model,
                          @RequestParam(value="pageName")String name,
                          @RequestParam(value = "param")String param){

        //没指明参数是什么时，默认为是用户id
        model.addAttribute("workmanid",Integer.parseInt(param));

        if(name.equals("userinfo")){
            //打开用户信息页面
            User user=userMapper.selectByPrimaryKey(Integer.parseInt(param));
            model.addAttribute("user",user);
        }else if(name.equals("addimage")){
            //打开新增图片页面
            Integer workid=Integer.parseInt(param);
            Work work= workServiceImpl.getWork(workid);
            Integer workmanid=work.getWorkmanid();
            ArrayList<String> images=new ArrayList<>();
            String ss=work.getPictures();
            if(ss!=null&&!ss.equals("")){
                String[] temp=ss.split(",");
                for(int i=0;i<temp.length;i++){
                    images.add(workmanid+"/"+temp[i]);
                }
            }
            model.addAttribute("images",images);
            model.addAttribute("workid",workid);
            model.addAttribute("workmanid",work.getWorkmanid());
        }else if(name.equals("workupload")){
            //打开上传实验页面
            Integer workid=Integer.parseInt(param);
            Work work= workServiceImpl.getWork(workid);
            model.addAttribute("workid",workid);
            model.addAttribute("workname",work.getWorkname());
        }
        return "admin/"+name;
    }

    /**
     * 获取某个作者的作品，有分页处理
     * @param manid
     * @param model
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/admin/getSomeoneWorks",method = RequestMethod.GET)
    @ResponseBody
    public Result showOnesWorks(@RequestParam("workmanid") Integer manid, Model model,
                                @RequestParam(value = "page",defaultValue = "1")int page,
                                @RequestParam(value = "limit",defaultValue = "5") int limit){
        PageHelper.startPage(page,limit,"id asc");
        Result result=new Result();
        ArrayList<Work> list=(ArrayList) workServiceImpl.getsomeoneWorks(manid);
        PageInfo<Work> datameta = new PageInfo<>(list);
        List<Work> works=datameta.getList();
        result.setCount((int)datameta.getTotal());
        result.setData(works);
        result.setCode(0);
        return result;
    }

    /**
     * 上传实验
     * @param workId
     * @param file
     * @param workId
     * @return
     */
    @RequestMapping(value="/admin/workupload",method = RequestMethod.POST)
    @ResponseBody
    public Result WorkUpload(@RequestParam("workid") Integer workId,
                             @RequestParam("files") MultipartFile file) throws Exception{
        Work work=workServiceImpl.getWork(workId);
        String workaddress=work.getWorkaddress();
        Result result=new Result();
        if(workaddress!=null&&workaddress.equals("")){
            result.setCode(400);
            return result;
        }
        //上传处理
        String path=imageAddress+File.separator+work.getWorkmanid();
        File workpath=new File(path);
        if(!workpath.exists()){
            workpath.mkdirs();
        }
        //获取文件后缀
        String filetype=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File workfile=new File(path+File.separator+work.getWorkname()+filetype);

        file.transferTo(workfile);

        work.setWorkaddress(work.getWorkname()+filetype);
        workServiceImpl.updateWork(work,false);
        result.setMsg("上传成功");
        return result;
    }

    /**
     * * 上传图片
     * * 虽然前端页面显示是多文件上传，实际上是单文件多次调用上传
     */
    @RequestMapping(value="/admin/uploadimg",method = RequestMethod.POST)
    @ResponseBody
    public Result ImagesUpload(@RequestParam("workid") Integer workId,
                               @RequestParam("files") MultipartFile file,
                               @RequestParam("workmanid") Integer workmanId) throws Exception{

        User user=userMapper.selectByPrimaryKey(workmanId);
        UUID uuid=UUID.randomUUID();
        String uuidstring=uuid.toString().substring(0,8);
        String filename=uuidstring+"."+file.getOriginalFilename().
                substring(file.getOriginalFilename().
                        lastIndexOf(".")+1);
        String path=imageAddress+File.separator+user.getId();
        File imagepath=new File(path);
        if(!imagepath.exists()){
            imagepath.mkdirs();
        }
        File imagefile=new File(path+File.separator+filename);

        file.transferTo(imagefile);
        //防止上传多张照片时发生并发错误
        synchronized (this) {
            Work work= workServiceImpl.getWork(workId);
            String pictures=work.getPictures();
            if (pictures == null || pictures.equals("")) {
                work.setPictures(filename);
            } else {
                work.setPictures(pictures + "," + filename);
            }
            workServiceImpl.updateWork(work, true);
        }

        Result result=new Result();
        result.setCode(0);
        result.setData(file.getOriginalFilename());
        return result;
    }

    /**
     * 创建一个实验
     * @param work
     * @return
     */
    @RequestMapping(value = "/admin/addwork",method = RequestMethod.POST)
    @ResponseBody
    public Result addWork(@RequestBody Work work) throws Exception{
        Result result=new Result();
        if(workServiceImpl.createNewWork(work)==1){
            return result;
        }else{
            result.setCode(400); //上传失败
        }
        return result;
    }


    /**
     * 删除实验图片
     * @param workId
     * @param img
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/delimg",method = RequestMethod.GET)
    @ResponseBody
    public Result delimg(@RequestParam("workid") Integer workId,
                         @RequestParam("img") String img) throws Exception{
        Result result=new Result();
        Work work=workServiceImpl.getWork(workId);
        String[] imgs=work.getPictures().split(",");
        //分离得到图片的文件名
        String[] temp=img.split("/");
        String imgname=temp[1];
        //得到图片的存放路径
        String imgpath=imageAddress+img;
        //更改表结构
        StringBuilder sb=new StringBuilder();
        if(imgs.length>1){
            for(String s:imgs){
                if(!s.equals(imgname)){
                    sb.append(s+",");
                }
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        work.setPictures(sb.toString());
        int sum=workServiceImpl.updateWork(work,false);
        //删除在服务器中的图片
        File file=new File(imgpath);
        boolean isdelete=file.delete();
        if(sum!=1||!isdelete){
            logger.error("图片文件:"+imgpath+" 删除失败！！！");
            result.setCode(400);
        }
        return result;
    }


    /**
     * 删除某个实验，包括图片和实验文件
     * @param workId
     * @return
     */
    @RequestMapping(value = "/admin/delwork",method = RequestMethod.GET)
    @ResponseBody
    public Result delwork(@RequestParam("id") Integer workId){
        Result result = new Result();
        Work work = workServiceImpl.getWork(workId);
        String picstr=work.getPictures();
        String[] pictures=picstr.split(",");
        //删除图片
        if(pictures.length>0){
            for(String pic: pictures){
                String strpath=imageAddress+"/"+work.getWorkmanid()+"/"+pic;
                File f=new File(strpath);
                f.delete();
            }
        }

        //删除实验
        String workpath=workAddress+"/"+work.getWorkmanid()+"/"+work.getWorkaddress();
        File workfile=new File(workpath);
        workfile.delete();

        workServiceImpl.delwork(workId);
        return result;
    }
    /**
     * 修改实验描述
     * @param workId
     * @param workcontent
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/admin/editwork",method = RequestMethod.GET)
    @ResponseBody
    public Result editwork(@RequestParam("id")Integer workId,
                           @RequestParam("workcontent")String workcontent) throws IOException {
        Result result = new Result();
        Work work = workServiceImpl.getWork(workId);
        work.setContent(workcontent);
        Integer i=workServiceImpl.updateWork(work,true);
        if(i!=1){
            logger.error("实验更新失败！！！");
            result.setCode(400);
        }
        return  result;
    }

}
