package qiutest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import unity.Application;
import unity.config.WorkIndex;
import unity.pojo.Work;
import unity.service.WorkServiceImpl;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@MapperScan(basePackages = "unity.dao" )
public class LuceneTest {
    @Autowired
    private WorkIndex workIndex;
    @Autowired
    private WorkServiceImpl workService;

    @Test
    public void test() throws IOException {
        List<Work> list= workService.getallWorks();
        for(Work work:list){
            workIndex.addIndex(work);
        }
        System.out.println("索引添加完成");
    }
}
