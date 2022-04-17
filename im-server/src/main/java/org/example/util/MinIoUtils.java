package org.example.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.HashMultimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.example.config.CourierConfig;
import org.example.config.Im;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*

docker run -d -p 9000:9000 -p 9001:9001 --name minio -v E:\minio\data:/data -e "MINIO_ROOT_USER=AKIAIOSFODNN7EXAMPLE" -e "MINIO_ROOT_PASSWORD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" --restart=always quay.io/minio/minio server /data --console-address ":9001"

docker run -d -p 9000:9000 -p 9001:9001 --name minio -v E:\minio\data:/data -v E:\minio\cert:/root/.minio -e "MINIO_ROOT_USER=AKIAIOSFODNN7EXAMPLE" -e "MINIO_ROOT_PASSWORD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" --restart=always quay.io/minio/minio server /data --console-address ":9001"

docker run -d -p 9000:9000 -p 9001:9001 --name minio -v E:/docker/minio/data:/data -v E:/docker/minio/cert:/root/.minio -e "MINIO_ROOT_USER=AKIAIOSFODNN7EXAMPLE" -e "MINIO_ROOT_PASSWORD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" --restart=always quay.io/minio/minio server /data --console-address ":9001"

docker run -d --name minio -v /home/minio/data:/data -v E:/docker/minio/cert:/root/.minio -e "MINIO_ROOT_USER=AKIAIOSFODNN7EXAMPLE" -e "MINIO_ROOT_PASSWORD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" --restart=always --net=host quay.io/minio/minio server /data --console-address ":9001"

 */


/**
 * minio 文件服务
 */
@Slf4j
public class MinIoUtils {

    static String MINIO_BUCKET = "courier";

    private static CustomMinioClient customMinioClient;

    static {
        MinIoUtils minIoUtils = new MinIoUtils();
        try {
            minIoUtils.init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("minio 初始化异常");
        }
    }

    public static InputStream download(String filePath) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(MINIO_BUCKET)
                .object(filePath)
                .build();
        try {
            return customMinioClient.getObject(args);
        } catch (ErrorResponseException |
                InsufficientDataException |
                InternalException |
                InvalidKeyException |
                InvalidResponseException |
                IOException |
                NoSuchAlgorithmException |
                ServerException |
                XmlParserException e) {
            log.error("文件下载失败：{}", e.getMessage());
        }
        return null;
    }

    public void init() throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(CourierConfig.minioHost,CourierConfig.minioPort,true)
                .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                .build();
        customMinioClient = new CustomMinioClient(minioClient);
        BucketExistsArgs build = BucketExistsArgs.builder().bucket(MINIO_BUCKET).build();
        boolean isExist = minioClient.bucketExists(build);
        if (!isExist) {
            //创建存储桶并设置只读权限
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(MINIO_BUCKET).build());

            String READ_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::" + MINIO_BUCKET + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + MINIO_BUCKET + "/*\"]}]}";
            SetBucketPolicyArgs build1 = SetBucketPolicyArgs.builder().bucket(MINIO_BUCKET).config(READ_ONLY).build();
            minioClient.setBucketPolicy(build1);
        }
    }

    /**
     * 单文件签名上传
     *
     * @param objectName 文件全路径名称
     * @return /
     */
    public static String getUploadObjectUrl(String objectName, String contentType) {
        // 上传文件时携带Content-Type头即可
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        try {
            return customMinioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(MINIO_BUCKET)
                            .object(objectName)
                            .expiry(1, TimeUnit.DAYS)
                            .extraHeaders(headers)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化分片上传
     *
     * @param objectName  文件全路径名称
     * @param partCount   分片数量
     * @param contentType 类型，如果类型使用默认流会导致无法预览
     * @return /
     */
    public static Map<String, Object> initMultiPartUpload(String objectName, int partCount, String contentType) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StrUtil.isBlank(contentType)) {
                contentType = "application/octet-stream";
            }
            HashMultimap<String, String> headers = HashMultimap.create();
            headers.put("Content-Type", contentType);
            String uploadId = customMinioClient.initMultiPartUpload(MINIO_BUCKET, null, objectName, headers, null);

            result.put("uploadId", uploadId);
            List<String> partList = new ArrayList<>();

            Map<String, String> reqParams = new HashMap<>();
            //reqParams.put("response-content-type", "application/json");
            reqParams.put("uploadId", uploadId);
            for (int i = 1; i <= partCount; i++) {
                reqParams.put("partNumber", String.valueOf(i));
                String uploadUrl = customMinioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(MINIO_BUCKET)
                                .object(objectName)
                                .expiry(1, TimeUnit.DAYS)
                                .extraQueryParams(reqParams)
                                .build());
//                String replace = uploadUrl.replace("http://8.142.64.52:7708", "http://127.0.0.1:7708");
                partList.add(uploadUrl);
            }
            result.put("uploadUrls", partList);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        return result;
    }

    /**
     * 分片上传完后合并
     *
     * @param objectName 文件全路径名称
     * @param uploadId   返回的uploadId
     * @return /
     */
    public static boolean mergeMultipartUpload(String objectName, String uploadId) {
        try {
            Part[] parts = new Part[1000];
            ListPartsResponse partResult = customMinioClient.listMultipart(MINIO_BUCKET, null, objectName, 1000, 0, uploadId, null, null);
            int partNumber = 1;
            for (Part part : partResult.result().partList()) {
                parts[partNumber - 1] = new Part(partNumber, part.etag());
                partNumber++;
            }
            customMinioClient.mergeMultipartUpload(MINIO_BUCKET, null, objectName, uploadId, parts, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean uploadByte(byte[] bytes, String name) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            customMinioClient.putObject(PutObjectArgs.builder().bucket(MINIO_BUCKET).contentType("application/octet-stream").stream(inputStream, bytes.length, 5368709119L).object(name).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException | InvalidResponseException | NoSuchAlgorithmException | IOException | XmlParserException | ServerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
