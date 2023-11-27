package cn.github.iocoder.dong.core.Interceptor;

import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.service.global.GlobalInitService;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring mvc拦截器
 */
@Slf4j
@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {

    @Autowired
    private GlobalInitService globalInitService;

    /**
     * 处理请求之前执行的。
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("zzzz");
        return true;
    }

    /**
     * 控制器处理请求之后，视图渲染之前执行的。
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //返回视图才进方法里面
        if (ObjectUtil.isNotNull(modelAndView)){
            if (response.getStatus() != HttpStatus.OK.value()) {
                try {
                    ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
                    // fixme 对于异常重定向到 /error 时，会导致登录信息丢失，待解决
//                    globalInitService.initLoginUser(reqInfo);
                    ReqInfoContext.addReqInfo(reqInfo);
                    modelAndView.getModel().put("global", globalInitService.globalAttr());
                } finally {
                    ReqInfoContext.clear();
                }
            } else {
                modelAndView.getModel().put("global", globalInitService.globalAttr());
            }
        }
    }
}
