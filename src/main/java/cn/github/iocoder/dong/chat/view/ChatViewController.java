package cn.github.iocoder.dong.chat.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "chat")
public class ChatViewController {
    @RequestMapping(path = {"", "/", "home"})
    public String index() {
        return "index";
    }
}
