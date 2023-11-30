package cn.github.iocoder.dong.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserVO implements Serializable, Cloneable{

    private String username;

    private String password;
}
