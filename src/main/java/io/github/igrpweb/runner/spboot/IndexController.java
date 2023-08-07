package io.github.igrpweb.runner.spboot;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author poolborges
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, RedirectAttributes redirectAttrs) {

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            for (Entry<String, ?> entry : flashMap.entrySet()) {
                redirectAttrs.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        redirectAttrs.addAttribute("from", "RedirectorSpringBoot");
        //return "redirect:index.jsp";
        return "index";
    }

    

    @RequestMapping(value = "/index.jsp", method = RequestMethod.GET)
    public String indexJSP(HttpServletRequest request, RedirectAttributes redirectAttrs) {

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            for (Entry<String, ?> entry : flashMap.entrySet()) {
                redirectAttrs.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        redirectAttrs.addAttribute("redirect_from", "RedirectorSpringBoot");
        return "index";
    }
}
