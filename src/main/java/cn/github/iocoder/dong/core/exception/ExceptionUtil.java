package cn.github.iocoder.dong.core.exception;


import cn.github.iocoder.dong.model.enums.StatusEnum;

/**
 * @author dong
 * @date 2022/9/2
 */
public class ExceptionUtil {

    public static ForumAdviceException of(StatusEnum status, Object... args) {
        return new ForumAdviceException(status, args);
    }

}
