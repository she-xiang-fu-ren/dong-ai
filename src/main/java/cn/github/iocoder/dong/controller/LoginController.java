package cn.github.iocoder.dong.controller;

import cn.github.iocoder.dong.core.utils.SessionUtil;
import cn.github.iocoder.dong.model.api.ResVo;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.service.user.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class LoginController {
    @Resource
    private UserService userService;

    @PostMapping("/login/username")
    public ResVo<Boolean> loginUsername(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "password") String password,
                                       HttpServletResponse response){
        String session = userService.register(username, password);
        if (StringUtils.isNotBlank(session)) {
            // cookie中写入用户登录信息，用于身份识别
            response.addCookie(SessionUtil.newCookie(userService.SESSION_KEY, session));
            return ResVo.ok(true);
        }
        return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "用户名和密码登录异常，请稍后重试");
    }


}
