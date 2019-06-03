package unity.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unity.interceptor.MyInterceptor;

/**
 * 注册拦截器，以及重新配置静态资源路径
 */
@Configuration
public class MySpringMVCConfig extends WebMvcConfigurationSupport {

    private static final Logger logger = LoggerFactory.getLogger(MySpringMVCConfig.class);

    @Autowired
    MyInterceptor myInterceptor;

    @Value("${work.address}")
    String workAddress;

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //配置拦截器的拦截路径和不拦截的路径
        registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
        logger.info("配置监听器完成！!");
        super.addInterceptors(registry);
    }

    /**
     * 使用了该类配置后，原默认或properties文件的静态资源配置会失效
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //网页需要的所有静态元素
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //实验保存的静态地址，供下载实验使用
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+workAddress);
        //图片保存的静态地址，供显示图片使用
        registry.addResourceHandler("/showimage/**").addResourceLocations("file:"+workAddress);
        logger.info("静态资源映射成功！!");
        super.addResourceHandlers(registry);
    }


    /**
     * 使用了该类配置后，首页设置会丢失，所以这里重置下
     * @param registry
     */
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("index");
        //registry.addViewController("/backstage").setViewName("admin/adminindex");
        //registry.addViewController("/houtai").setViewName("root/rootindex");
        super.addViewControllers(registry);
    }



}
