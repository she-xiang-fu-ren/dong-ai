package cn.github.iocoder.dong.service.image.oss.impl;


import cn.github.iocoder.dong.core.config.properties.OSSProperties;
import cn.github.iocoder.dong.core.utils.Md5Util;
import cn.github.iocoder.dong.service.image.oss.ImageUploader;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@ConditionalOnExpression(value = "#{'ali'.equals(environment.getProperty('dong.image.type'))}")
public class AiliOssImagesServiceImpl implements ImageUploader, InitializingBean, DisposableBean {
    private static final int SUCCESS_CODE = 200;
    private OSS ossClient;

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
        // 创建PutObjectRequest对象。
        InputStream inputStream =null;
        try {
             inputStream= file.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            return upload(bytes,fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    //流没关闭，需要主动关闭流，否则会抛异常
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭刘除了异常");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String upload(byte[] bytes, String fileType) {
        try {
            String fileName = Md5Util.encode(bytes);
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            fileName = ossProperties.getPrefix() + fileName + "." + fileType;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), fileName, input);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");

            //上传文件
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            if (SUCCESS_CODE == result.getResponse().getStatusCode()){
                return ossProperties.getHost() + fileName;
            } else {
                log.error("upload to oss error! response:{}", result.getResponse().getStatusCode());
                return null;
            }
        }catch (OSSException e){
            log.error("Oss rejected with an error response! msg:{}, code:{}, reqId:{}, host:{}", e.getErrorMessage(), e.getErrorCode(), e.getRequestId(), e.getHostId());
            return null;
        }catch (Exception ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network. {}", ce.getMessage());
            return null;
        } finally {
        }
    }

    /**
     * bean销毁前调用
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (ossClient!=null){
            ossClient.shutdown();
        }
    }

    /**
     * bean初始化前调用
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init(){
        ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(),ossProperties.getAccessKey(),ossProperties.getSecretKey());
    }
}
