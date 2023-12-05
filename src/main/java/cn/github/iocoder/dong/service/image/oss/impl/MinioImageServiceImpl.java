package cn.github.iocoder.dong.service.image.oss.impl;

import cn.github.iocoder.dong.core.config.properties.OSSProperties;
import cn.github.iocoder.dong.core.utils.Md5Util;
import cn.github.iocoder.dong.service.image.oss.ImageUploader;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 使用minio实现图片上传
 */
@Component
@ConditionalOnExpression(value = "#{'minio'.equals(environment.getProperty('dong.image.type'))}")
public class MinioImageServiceImpl implements ImageUploader , InitializingBean {

    private MinioClient minioClient;

    @Autowired
    private OSSProperties ossProperties;

    /**
     * 上传文件/图片
     *
     * @param file
     * @param fileType
     * @return
     */
    @Override
    public String upload(MultipartFile file, String fileType) {
        String fileName = null;
        try {
            fileName = Md5Util.encode(StreamUtils.copyToByteArray(file.getInputStream()));
            minioClient.putObject(PutObjectArgs.builder().bucket(ossProperties.getBucketName()).stream(file.getInputStream(),file.getSize(),-1).object(fileName).contentType(fileType).build());
            return ossProperties.getEndpoint()+"/"+ossProperties.getBucketName()+"/"+fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bean初始化前的操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init(){
        //创建Minio实例
        minioClient = MinioClient.builder().endpoint(ossProperties.getEndpoint()).credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey()).build();
    }
}
