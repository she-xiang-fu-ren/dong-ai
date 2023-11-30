package cn.github.iocoder.dong.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录结果的枚举类
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum {

    SUCCESS("登陆成功"), // 成功
    BAD_CREDENTIALS("账号或密码不正确"), // 账号或密码不正确
    USER_DISABLED("用户被禁用"), // 用户被禁用
    CAPTCHA_NOT_FOUND("图片验证码不存在"), // 图片验证码不存在
    CAPTCHA_CODE_ERROR("图片验证码不正确"), // 图片验证码不正确

    ;

    /**
     * 结果
     */
    private final String result;

}
