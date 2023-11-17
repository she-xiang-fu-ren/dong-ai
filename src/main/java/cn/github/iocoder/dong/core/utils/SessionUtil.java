package cn.github.iocoder.dong.core.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.util.List;

public class SessionUtil {

    private static final int COOKIE_AGE = 30 * 86400;

    public static Cookie newCookie(String key, String session) {
        return newCookie(key, session, "/", COOKIE_AGE);
    }

    public static Cookie newCookie(String key, String session, String path, int maxAge) {
        Cookie cookie = new Cookie(key, session);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie delCookie(String key) {
        return delCookie(key, "/");
    }

    public static Cookie delCookie(String key, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static String findCookieByName(ServerHttpRequest request, String name) {
        List<String> list = request.getHeaders().get("cookie");
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        for (String sub : list) {
            String[] elements = StringUtils.split(sub, ";");
            for (String element : elements) {
                String[] subs = StringUtils.split(element, "=");
                if (subs.length == 2 && ObjectUtil.equals(subs[0].trim(), name)) {
                    return subs[1].trim();
                }
            }
        }
        return null;
    }
}
