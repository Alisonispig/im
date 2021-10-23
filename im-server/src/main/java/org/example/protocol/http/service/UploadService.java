package org.example.protocol.http.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.minio.GetObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.example.util.MinIoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tuine
 * @date 2021/3/23
 */
public class UploadService {

    public static Map<String, Object> initMultiPartUpload(String path, String filename, Integer partCount, String contentType) {
        path = path.replaceAll("/+", "/");
        if (path.indexOf("/") == 0) {
            path = path.substring(1);
        }
        String filePath = path + "/" + IdUtil.getSnowflake().nextIdStr() + '.' + FileNameUtil.extName(filename);

        Map<String, Object> result = new HashMap<>();
        // TODO::单文件上传可拆分，这里只做演示，可直接上传完成
        if (partCount == 1) {
            String uploadObjectUrl = MinIoUtils.getUploadObjectUrl(filePath);
            result.put("uploadUrls", ImmutableList.of(uploadObjectUrl));
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
}
