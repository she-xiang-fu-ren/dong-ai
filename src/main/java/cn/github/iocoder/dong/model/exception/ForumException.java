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
public class ForumException extends RuntimeException {
    @Getter
    private Status status;

    public ForumException(Status status) {
        this.status = status;
    }

    public ForumException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public ForumException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

}
