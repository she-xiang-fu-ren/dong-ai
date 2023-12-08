package cn.github.iocoder.dong.controller;

import cn.github.iocoder.dong.controller.vo.UserInfoVO;
import cn.github.iocoder.dong.controller.vo.UserVO;
import cn.github.iocoder.dong.core.permission.Permission;
import cn.github.iocoder.dong.core.permission.UserRole;
import cn.github.iocoder.dong.model.api.ResVo;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;

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

    @PostMapping("/saveUserInfo")
    @Permission(role = UserRole.LOGIN)
    public ResVo<Boolean> saveUserInfo(@RequestBody UserInfoVO userInfoVO){
        if (userInfoVO.getUserId() == null || !Objects.equals(userInfoVO.getUserId(), ReqInfoContext.getReqInfo().getUserId())) {
            // 不能修改其他用户的信息
            return ResVo.fail(StatusEnum.FORBID_ERROR_MIXED, "无权修改");
        }
        userService.saveUserInfo(userInfoVO);
        return ResVo.ok(true);
    }
}
