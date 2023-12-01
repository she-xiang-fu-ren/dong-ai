package cn.github.iocoder.dong.service.user.service;

import javax.servlet.http.HttpServletRequest;

public interface ImageService {
    /**
     * 上次图片
     * @param request
     * @return
     */
    String saveImg(HttpServletRequest request);
}
