package unity.dao;

import java.util.List;
import unity.pojo.Assess;

public interface AssessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Assess record);

    Assess selectByPrimaryKey(Integer id);

    List<Assess> selectAll();

    int updateByPrimaryKey(Assess record);

    List<Assess> selectByWorkId(Integer workId);
}