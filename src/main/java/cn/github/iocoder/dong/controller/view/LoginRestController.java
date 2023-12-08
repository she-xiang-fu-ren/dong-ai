package cn.github.iocoder.dong.controller.view;

import cn.github.iocoder.dong.controller.vo.UserInfoVO;
import cn.github.iocoder.dong.core.permission.Permission;
import cn.github.iocoder.dong.core.permission.UserRole;
import cn.github.iocoder.dong.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class LoginRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"", "/"})
    public String index(Model model,HttpSession httpSession) {
        return "home/index";
    }

    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/user/home")
    public String getUserHome(@RequestParam(name = "userId") Long userId, Model model, HttpServletRequest httpServletRequest) {
        UserInfoVO userInfoVO =  userService.queryUserInfo(userId,httpServletRequest);
        model.addAttribute("vo",userInfoVO);
        return "user/index";
    }

}
