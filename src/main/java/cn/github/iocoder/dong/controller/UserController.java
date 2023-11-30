package cn.github.iocoder.dong.controller;

import cn.github.iocoder.dong.controller.vo.UserVO;
import cn.github.iocoder.dong.model.api.ResVo;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
    public ResVo<Boolean> createUser(UserVO userVO){
        String user = userService.createUser(userVO.getUsername(), userVO.getPassword());
        if (StringUtils.isNotBlank(user)){
            return ResVo.ok(true);
        }
        return ResVo.fail(StatusEnum.LOGIN_FAILED_SIGN, "用户名和密码注册异常，请稍后重试");
    }
}
