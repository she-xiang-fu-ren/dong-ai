package cn.github.iocoder.dong.model.context.dto;

import cn.github.iocoder.dong.model.api.BaseDO;
import lombok.Data;

@Data
public class UserDTO extends BaseDO {

    /**
     * 账号
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 照片地址
     */
    private String photoUrl;
}
