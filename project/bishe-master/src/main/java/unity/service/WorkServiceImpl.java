package unity.service;

import unity.config.WorkIndex;
import unity.dao.UserMapper;
import unity.dao.WorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unity.pojo.Work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkServiceImpl {

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkIndex workIndex;

    /**
     * 通过id查询一个实验
     * @param workid
     * @return
     */
    public Work getWork(Integer workid){
        return workMapper.selectByPrimaryKey(workid);
    }

    /**
     * 更新一个实验
     * @param work
     * @return
     */
    public int updateWork(Work work,boolean isneedIndex) throws IOException {
        if(!isneedIndex){      //假如是图片和实验的上传删除操作、评论操作就不用更新索引
            workIndex.updateIndex(work);
        }
        return workMapper.updateByPrimaryKey(work);
    }

    /**
     * 查询所有实验
     */
    public List<Work> getallWorks(){
        ArrayList<Work> list=(ArrayList)workMapper.selectAll();
        return list;
    }

    /**
     * 查询某个人的实验
     */
    public List<Work> getsomeoneWorks(Integer workmanid){
        ArrayList<Work> list=(ArrayList)workMapper.selectByWorkmanId(workmanid);
        return list;
    }

    /**
     * 查询某个分类的实验
     */
    public List<Work> getWorksByType(Integer type){
        ArrayList<Work> list=(ArrayList)workMapper.selectByType(type);
        return list;
    }

    /**
     * 创建新实验
     * @param work
     * @return
     */
    public int createNewWork (Work work) throws Exception{
        int result=workMapper.insert(work);
        workIndex.addIndex(work);
        return result;
    }

    /**
     * 通過实验找作者名稱
     * @param workid
     * @return
     */
    public String findWorkname(Integer workid){
        Integer workmanid=workMapper.selectByPrimaryKey(workid).getWorkmanid();
        String manname=userMapper.selectByPrimaryKey(workmanid).getUsername();
        return manname;
    }

    /**
     * 查找所有免费实验
     * @return
     */
    public List<Work> getallfree(){
        ArrayList<Work> list=(ArrayList)workMapper.getfree();
        return list;
    }


    /**
     * 获取数据库中实验数目
     * @return
     */
    public Integer getsum(){
        return workMapper.getsum();
    }


    /**
     * 删除实验操作
     * @param workid
     * @return
     */
    public Integer delwork(Integer workid){
        return workMapper.deleteByPrimaryKey(workid);
    }

}
