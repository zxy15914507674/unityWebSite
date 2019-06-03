package unity.dao;


import unity.pojo.Complain;

import java.util.List;

public interface ComplainMapper {


    int insert(Complain record);

    List<Complain> selectAll();

}