package unity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 国际化类，在main方法处注入
 */
public class MylocaleResolver implements LocaleResolver {

    private static final Logger logger = LoggerFactory.getLogger(MylocaleResolver.class);
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getParameter("language");
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(language)){
            String[] split = language.split("_");
            locale = new Locale(split[0],split[1]);
            HttpSession session = request.getSession();
            session.setAttribute("I18N_LANGUAGE_SESSION", locale);
        }else{
            //如果没有带国际化参数，则判断session有没有保存，有保存，则使用保存的，也就是之前设置的，避免之后的请求不带国际化参数造成语言显示不对
            HttpSession session = request.getSession();
            Locale localeInSession = (Locale) session.getAttribute("I18N_LANGUAGE_SESSION");
            if(localeInSession != null) {
                locale = localeInSession;
            }
        }
        logger.info("动态国际化切换配置成功");
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
