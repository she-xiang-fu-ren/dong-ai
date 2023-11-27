package cn.github.iocoder.dong.service.global.vo;

import cn.github.iocoder.dong.model.context.dto.UserDTO;
import lombok.Data;

@Data
public class GlobalVo {

    /**
     * 在线用户人数
     */
    private Integer onlineCnt;
    /**
     * 是否已登录
     */
    private Boolean isLogin;

    private UserDTO user;

}
