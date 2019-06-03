package unity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unity.dao.AssessMapper;
import unity.pojo.Assess;

import java.util.List;

@Service
public class AssessServiceImpl {

    @Autowired
    AssessMapper assessMapper;

    public List<Assess> getAssessByWorkId(Integer workId){
        List<Assess> list=assessMapper.selectByWorkId(workId);
        return list;
    }

    public int createAssess(Assess assess){
        return assessMapper.insert(assess);
    }
}
