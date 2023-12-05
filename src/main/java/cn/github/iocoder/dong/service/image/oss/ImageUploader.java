package cn.github.iocoder.dong.service.image.oss;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    /**
     * 上传文件/图片
     * @param file
     * @param fileType
     * @return
     */
    String upload(MultipartFile file, String fileType);
}
