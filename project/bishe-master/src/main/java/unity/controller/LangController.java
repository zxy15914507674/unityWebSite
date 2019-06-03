package unity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class LangController {

    @RequestMapping("/changelang")
    public String changlang(HttpServletRequest request, String language){
        if("zh".equals(language)) {
            request.getSession().setAttribute(
                    SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                    new Locale("zh","CN"));
        }else if("en".equals(language)){
            request.getSession().setAttribute(
                    SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                    new Locale("en","US"));
        }
        return "redirect:/";
    }
}
