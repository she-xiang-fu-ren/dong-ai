package cn.github.iocoder.dong.controller;

import cn.github.iocoder.dong.model.api.ResVo;
import cn.github.iocoder.dong.service.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/create")
    public ResVo<Boolean> createUser(@RequestParam(name = "username") String username,
                                     @RequestParam(name = "password") String password){
        userService.createUser(username,password);
        return ResVo.ok(true);
    }
}
