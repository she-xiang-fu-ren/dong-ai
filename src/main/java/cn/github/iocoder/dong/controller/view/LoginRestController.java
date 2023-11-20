package cn.github.iocoder.dong.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginRestController {

    @RequestMapping(path = {"", "/"})
    public String index(Model model) {
        return "index";
    }
}
