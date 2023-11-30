package cn.github.iocoder.dong.service.user.repository.entity;

import cn.github.iocoder.dong.model.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

@TableName(value = "gpt_login_log", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginLogDO extends BaseDO {

private static final long serialVersionUID = 778562368167475189L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录结果
     */
    private String result;

    /**
     * 用户IP
     */
    private String userIp;

    /**
     * IP归属
     */
    private String ipVest;

    /**
     * 浏览器 UA
     */
    private String userAgent;

}

