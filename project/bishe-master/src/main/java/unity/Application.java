package unity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;


@SpringBootApplication
@MapperScan(basePackages = "unity.dao" )
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
        //SpringApplication.run(Application.class,args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr =new SessionLocaleResolver();
        //设置默认区域,
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }
}
