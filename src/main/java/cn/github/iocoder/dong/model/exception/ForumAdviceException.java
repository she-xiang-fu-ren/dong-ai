package cn.github.iocoder.dong.model.exception;


import cn.github.iocoder.dong.model.api.Status;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author dong
 * @date 2022/9/2
 */
public class ForumAdviceException extends RuntimeException {
    @Getter
    private Status status;

    public ForumAdviceException(Status status) {
        this.status = status;
    }

    public ForumAdviceException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public ForumAdviceException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

}
