package io.github.igrpweb.runner.spboot;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author poolb
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(HttpServletRequest request, RedirectAttributes redirectAttrs) {

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            for (Map.Entry<String, ?> entry : flashMap.entrySet()) {
                redirectAttrs.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        redirectAttrs.addAttribute("from", "RedirectorSpringBoot");
        //return "redirect:error.jsp";
        return "error";
    }
    
    @RequestMapping(value = "/error.jsp", method = RequestMethod.GET)
    public String errorJSP(HttpServletRequest request, RedirectAttributes redirectAttrs) {

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            for (Entry<String, ?> entry : flashMap.entrySet()) {
                redirectAttrs.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        redirectAttrs.addAttribute("from", "RedirectorSpringBoot");
        return "error";
    }
}
