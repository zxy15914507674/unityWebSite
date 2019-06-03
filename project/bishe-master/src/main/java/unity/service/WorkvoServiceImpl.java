package unity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unity.pojo.User;
import unity.pojo.Work;
import unity.vo.Workvo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkvoServiceImpl {

    //详情页模式
    public static Integer WORKDETAIL_MODE=0;

    //结果页模式
    public static Integer WORKSRESULT_MODE=1;

    @Autowired
    UserServiceImpl userService;

    /**
     * 将pojo转为vo
     * @param work
     * @return
     */
    public  Workvo pojoTovo(Work work,Integer mode) {

        if (mode == WORKSRESULT_MODE) {     //结果页
            Workvo workvo = new Workvo();
            String content = work.getContent();
            if (content != null && content.length() > 20) {
                content = content.substring(0, 20)+"...";
            }
            workvo.setWorkid(work.getId());
            workvo.setForfree(work.getForfree());
            workvo.setContent(content);
            workvo.setWorkname(work.getWorkname());
            String images = work.getPictures();
            if (images != null && !"".equals(images)) {
                String[] imagelist = images.split(",");
                workvo.setIndeximage(work.getWorkmanid() + "/" + imagelist[0]);
            } else {
                workvo.setIndeximage(null);
            }
            return workvo;
        } else if (mode == WORKDETAIL_MODE) {    //详情页
            Workvo workvo = new Workvo();
            workvo.setWorkid(work.getId());
            workvo.setContent(work.getContent());
            workvo.setWorkname(work.getWorkname());
            workvo.setForfree(work.getForfree());

            List<String> imagelist = new ArrayList<>();
            String pictures=work.getPictures();
            if(pictures!=null&&!"".equals(pictures)){
                String[] temp = pictures.split(",");
                if(temp.length>1||!temp[0].equals("")){
                    for (String image : temp) {
                        imagelist.add(work.getWorkmanid() + "/" + image);
                    }
                }
            }
            workvo.setImagelist(imagelist);

            workvo.setKeyword(work.getKeyword());
            workvo.setScore(work.getScore());
            workvo.setKeyword(work.getKeyword());

            String workaddress=work.getWorkaddress();
            if(workaddress!=null&&!"".equals(workaddress)){
                workvo.setDladdress(work.getWorkmanid() + "/"+ workaddress);
            }

            User user=userService.getUser(work.getWorkmanid());
            workvo.setMaker(user.getUsername());
            switch (work.getWorktype()) {
                case 1:
                    workvo.setType("化学");
                    break;
                case 2:
                    workvo.setType("物理");
                    break;
                case 3:
                    workvo.setType("生物");
                    break;
                default:
                    workvo.setType(null);
            }
            return workvo;
        }else{
            System.out.println("函数调用错误");
            return null;
        }
    }


    /**
     * 分页时计算起始页
     * @param currentpage  当前页数
     * @param pagebutton   分页按钮数目
     * @return
     */
    public long pageprocess(long currentpage,int pagebutton){
        long startpage;
        if(currentpage%pagebutton==0){
            startpage=currentpage-pagebutton+1;
        }else{
            startpage=currentpage%pagebutton==1?currentpage:(currentpage+1-currentpage%pagebutton);
        }
        return startpage;
    }
}
