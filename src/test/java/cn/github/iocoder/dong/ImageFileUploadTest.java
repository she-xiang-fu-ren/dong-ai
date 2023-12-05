package cn.github.iocoder.dong;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
public class ImageFileUploadTest {
//    public static void main(String[] args) {
//        try {
//            // 创建一个MinIO客户端实例。
//            MinioClient minioClient = MinioClient.builder()
//                    .endpoint("http://175.178.3.46:9000")
//                    .credentials("admin", "admin123")
//                    .build();
//
//            // 指定要上传的文件。
//            File file = new File("D:\\Idea2023\\dong-ai\\src\\main\\resources\\static\\img\\icon.png");
//            // 创建一个输入流，用于读取文件内容。
//            FileInputStream fis = new FileInputStream(file);
//
//            // 创建一个对象存储桶（如果它还不存在）。
//            String bucketName = "dong";
//            // 上传文件到对象存储桶。我们使用文件名作为对象名。
//            String objectName = file.getName();
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(objectName)
//                    .stream(fis, file.length(), -1) // 第三个参数是chunk大小，-1表示整个文件。
//                    .build());
//            System.out.println("File uploaded successfully.");
//        } catch (Exception e) {
//            System.err.println("Error occurred: " + e);
//        }
//    }
}
