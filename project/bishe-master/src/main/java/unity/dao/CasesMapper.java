package unity.dao;


import unity.pojo.Cases;

import java.util.List;

public interface CasesMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Cases record);

    List<Cases> selectAll();
}
