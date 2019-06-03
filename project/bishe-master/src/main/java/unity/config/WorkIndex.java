package unity.config;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import unity.pojo.Work;
import unity.service.WorkServiceImpl;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


/**
 * lucene索引类
 */
@Component
public class WorkIndex {

    private static final Logger logger = LoggerFactory.getLogger(WorkIndex.class);

    //存放索引的物理位置
    @Value("${lucene.adress}")
    public  String indexDir;

    public Directory directory;

    @Autowired
    private WorkServiceImpl workService;

    /**
     * 获取写索引对象
     * @return
     * @throws IOException
     */
    public IndexWriter getIndexWriter() throws IOException {
        //实例化索引目录
        directory = FSDirectory.open(Paths.get(indexDir));
        //得到中文解析器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        //注册中文解析器到写索引配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //实例化写索引对象
        IndexWriter indexWriter = new IndexWriter(directory,config);
        return indexWriter;
    }

    /**
     * 添加实验索引
     * @param work
     * @throws IOException
     */
    public void addIndex(Work work) throws IOException {
        //获取写索引对象
        IndexWriter indexWriter = getIndexWriter();
        //实例化索引文档
        Document document = new Document();
        //设置索引文件字段
        document.add(new StringField("id",String.valueOf(work.getId()), Field.Store.YES));
        document.add(new TextField("workname",work.getWorkname(), Field.Store.YES));
        document.add(new TextField("keyword",work.getKeyword(), Field.Store.YES));
        document.add(new TextField("content",work.getContent(), Field.Store.YES));
        //索引文档加入到写索引中
        indexWriter.addDocument(document);
        //关闭写索引
        indexWriter.close();
    }

    /**
     * 删除指定实验id的索引
     * @param workid
     */
    public void deleteIndex(String workid) throws IOException {
        IndexWriter indexWriter = getIndexWriter();
        indexWriter.deleteDocuments(new Term("id",workid));
        indexWriter.forceMergeDeletes();//强制删除
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 更新实验索引文件
     * @param work
     * @throws IOException
     */
    public void updateIndex(Work work) throws IOException {
        //获取写索引对象
        IndexWriter indexWriter = getIndexWriter();
        //实例化索引文档
        Document document = new Document();
        //设置索引文件字段
        document.add(new StringField("id",String.valueOf(work.getId()), Field.Store.YES));
        document.add(new TextField("workname",work.getWorkname(), Field.Store.YES));
        document.add(new TextField("keyword",work.getKeyword(), Field.Store.YES));
        document.add(new TextField("content",work.getContent(), Field.Store.YES));
        //更新索引文档
        indexWriter.updateDocument(new Term("id",String.valueOf(work.getId())),document);
        //关闭写索引
        indexWriter.close();
    }

    public List<Work> searchWork(String q) throws Exception{
        //实例化目录对象
        directory = FSDirectory.open(Paths.get(indexDir));
        //获取读索引对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //获取索引查询对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //组合查询对象
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        //中文分析器
        SmartChineseAnalyzer chineseAnalyzer = new SmartChineseAnalyzer();

        //标题查询分析器
        QueryParser titleParser = new QueryParser("workname",chineseAnalyzer);
        //标题查询器
        Query titleQuery = titleParser.parse(q);

        //关键词查询分析器
        QueryParser keywordParser = new QueryParser("keyword",chineseAnalyzer);
        //关键词查询器
        Query keywordQuery = keywordParser.parse(q);

        //内容查询分析器
        QueryParser contentParser = new QueryParser("content",chineseAnalyzer);
        //内容查询器
        Query contentQuery = contentParser.parse(q);

        //添加标题查询器  逻辑关系为或者
        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
        //添加关键词查询器  逻辑关系为或者
        builder.add(keywordQuery, BooleanClause.Occur.SHOULD);
        //添加内容查询器  逻辑关系为或者
        builder.add(contentQuery, BooleanClause.Occur.SHOULD);

        //查询前100条记录放在his中
        TopDocs hits = indexSearcher.search(builder.build(),100);
        //将title得分高的放在前面
        //QueryScorer queryScorer = new QueryScorer(titleQuery);
        //得分高的片段
        //Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        //格式化得分高片段
        //SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("", "");
        //高亮显示
        //Highlighter highlighter = new Highlighter(formatter,queryScorer);
        //将得分高的片段设置进去
        //highlighter.setTextFragmenter(fragmenter);
        //用来封装查询到的博客
        List<Work> workList = new LinkedList<>();
        //遍历查询结果
        for(ScoreDoc score : hits.scoreDocs) {
            Document doc = indexSearcher.doc(score.doc);
            Work work = workService.getWork(Integer.parseInt(doc.get("id")));
            workList.add(work);
        }
        return workList;
    }

    /**
     * 初始化操作建立索引库，避免查询时出现异常
     */
    @PostConstruct
    public void init(){
        try{
            if(workService.getsum()==0){
                IndexWriter indexWriter=getIndexWriter();
                indexWriter.commit();
                indexWriter.close();
                logger.info("初始化Lucene索引成功");
            }
        }catch(Exception e){
            logger.error("初始化Lucene索引失败");
        }
    }
}
