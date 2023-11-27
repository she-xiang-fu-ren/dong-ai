package cn.github.iocoder.dong.core.filter;

import cn.github.iocoder.dong.core.utils.IpUtil;
import cn.github.iocoder.dong.core.utils.SessionUtil;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.service.global.GlobalInitService;
import cn.github.iocoder.dong.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * 过滤器
 */
@WebFilter(urlPatterns = "/*", filterName = "reqRecordFilter", asyncSupported = true)
public class ReqRecordFilter implements Filter {

    @Autowired
    private GlobalInitService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        try {
            reqInfo.setClientIp(IpUtil.getClientIp(request));
            reqInfo.setHost(request.getHeader("host"));
            reqInfo.setPath(request.getPathInfo());
            if (reqInfo.getPath() == null) {
                String url = request.getRequestURI();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                reqInfo.setPath(url);
            }
            reqInfo.setUserAgent(request.getHeader("User-Agent"));
            Optional.ofNullable(SessionUtil.findCookieByName(request, "f-session"))
                    .ifPresent(cookie -> userService.initLoginUser(cookie.getValue(), reqInfo));
            ReqInfoContext.addReqInfo(reqInfo);
            //日志、验证（是否登录）
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            ReqInfoContext.clear();
        }
    }

    @Override
    public void destroy() {

    }
}
