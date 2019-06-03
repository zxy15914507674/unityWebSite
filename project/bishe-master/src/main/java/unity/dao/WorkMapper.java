package unity.dao;

import java.util.List;
import unity.pojo.Work;
import unity.vo.Workvo;

public interface WorkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Work record);

    Work selectByPrimaryKey(Integer id);

    List<Work> selectAll();

    int updateByPrimaryKey(Work record);

    List<Work> selectByWorkmanId(Integer id);

    List<Work> selectByType(Integer type);

    List<Work> getfree();

    Integer getsum();
}