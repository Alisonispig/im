package org.example.protocol.http.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableList;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.DefaultEnum;
import org.example.service.FileService;
import org.example.util.MinIoUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传服务
 */
public class UploadService {
    private static final FileService fileService = new FileService();

    public static Map<String, Object> initMultiPartUpload(String filename, Integer partCount, String contentType) {

        String filePath = IdUtil.getSnowflake().nextIdStr() + '.' + FileNameUtil.extName(filename);

        Map<String, Object> result = new HashMap<>();
        if (partCount == 1) {
            String uploadObjectUrl = MinIoUtils.getUploadObjectUrl(filePath,contentType);
            if(uploadObjectUrl != null) {
                result.put("uploadUrls", ImmutableList.of(uploadObjectUrl));
            }
        } else {
            result = MinIoUtils.initMultiPartUpload(filePath, partCount, contentType);
        }
        result.put("objectName", filePath);
        return result;
    }

    public static boolean mergeMultipartUpload(String objectName, String uploadId) {
        return MinIoUtils.mergeMultipartUpload(objectName, uploadId);
    }

    public static InputStream downloadGetStream(String filePath) {
        return MinIoUtils.download(filePath);
    }

    public static boolean uploadFile(byte[] bytes, String name) {
        return MinIoUtils.uploadByte(bytes, name);
    }

    public static String uploadDefault(DefaultEnum defaultEnum) {
        String url = fileService.getFileUrl(defaultEnum.getKey());
        if (StrUtil.isBlank(url)) {
            byte[] bytes = ResourceUtil.readBytes("img/" + defaultEnum.getValue());
            boolean b = uploadFile(bytes, defaultEnum.getValue());
            if (b) {
                fileService.setFileUrl(defaultEnum.getKey(), defaultEnum.getValue());
            }
        }
        return Im.fileUrl + defaultEnum.getValue();
    }
}
