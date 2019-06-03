package unity.dao;

import unity.pojo.News;

import java.util.List;

public interface NewsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(News record);

    List<News> selectAll();
}
