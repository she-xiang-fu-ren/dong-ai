package cn.github.iocoder.dong.model.context;

import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import lombok.Data;

import java.security.Principal;

/**
 * 请求上下文，携带用户身份相关信息
 *
 * @author dong
 * @date 2022/7/6
 */
public class ReqInfoContext {
    private static ThreadLocal<ReqInfo> contexts = new InheritableThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    public static class ReqInfo implements Principal {
        /**
         * 客户端ip
         */
        private String clientIp;

        /**
         * 登录的会话
         */
        private String session;

        /**
         * 访问的域名
         */
        private String host;
        /**
         * 访问路径
         */
        private String path;

        /**
         * 设备信息
         */
        private String userAgent;

        private String ai;

        /**
         * 用户id
         */
        private Long userId;

        private UserDO user;

        @Override
        public String getName() {
            return session;
        }
    }
}
