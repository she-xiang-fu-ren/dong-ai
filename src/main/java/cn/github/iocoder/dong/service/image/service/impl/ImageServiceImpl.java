package cn.github.iocoder.dong.service.image.service.impl;

import cn.github.iocoder.dong.core.exception.ExceptionUtil;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.service.image.oss.ImageUploader;
import cn.github.iocoder.dong.service.image.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Service
public class ImageServiceImpl  implements ImageService {


    @Resource
    private ImageUploader imageUploader;

    /**
     * 上传图片
     *
     * @param request
     * @return
     */
    @Override
    public String saveImg(HttpServletRequest request) {
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) request).getFile("image");
        }
        if (file == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "缺少需要上传的图片");
        }
        // 目前只支持 jpg, png, webp 等静态图片格式
        String fileType = validateStaticImg(file.getContentType());
        if (fileType == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "图片只支持png,jpg,gif");
        }
        return imageUploader.upload(file, fileType);
    }

    /**
     * 图片格式校验
     *
     * @param mime
     * @return
     */
    private String validateStaticImg(String mime) {
        if (mime.contains("jpg")) {
            mime = mime.replace("jpg", "jpeg");
        }
        if ("svg".equalsIgnoreCase(mime)||"image/jpeg".equals(mime)||"image/gif".equals(mime)||"image/png".equals(mime)) {
            // fixme 上传文件保存到服务器本地时，做好安全保护, 避免上传了要给攻击性的脚本
            switch (mime){
                case "svg":
                    return mime;
                case "image/jpeg":
                    return mime.replace("image/jpeg","jpg");
                case "image/gif":
                    return mime.replace("image/gif","gif");
                case "image/png":
                    return mime.replace("image/png","png");
            }
        }
        return null;
    }
}
