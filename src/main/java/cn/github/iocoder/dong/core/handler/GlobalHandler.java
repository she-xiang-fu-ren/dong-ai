package cn.github.iocoder.dong.core.handler;

import cn.github.iocoder.dong.core.exception.ForumAdviceException;
import cn.github.iocoder.dong.model.api.ResVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return ResVo
     */
    @ExceptionHandler(ForumAdviceException.class)
    public ResVo<String> doBaseExceptionHandler(ForumAdviceException e) {
        return ResVo.fail(e.getStatus());
    }
}
