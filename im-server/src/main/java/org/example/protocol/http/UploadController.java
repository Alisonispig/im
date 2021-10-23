package org.example.protocol.http;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import org.example.packets.file.FileInit;
import org.example.packets.file.FileMerge;
import org.example.protocol.http.service.UploadService;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;
import org.tio.utils.jfinal.P;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@RequestPath
public class UploadController {

    @RequestPath("/multipart/init")
    public HttpResponse initMultiPartUpload(HttpRequest httpRequest) {
        FileInit fileInit = JSONObject.parseObject(httpRequest.getBodyString(), FileInit.class);

        // 路径
        String path = ObjectUtil.defaultIfNull(fileInit.getPath(), "minio");
        // 文件名
        String filename = ObjectUtil.defaultIfNull(fileInit.getFilename(), "");
        // content-type
        String contentType = ObjectUtil.defaultIfNull(fileInit.getContentType(), "application/octet-stream");
        // md5-可进行秒传判断
        String md5 = ObjectUtil.defaultIfNull(fileInit.getMd5(), "");
        // 分片数量
        Integer partCount = ObjectUtil.defaultIfNull(fileInit.getPartCount(), 1);

        Map<String, Object> result = UploadService.initMultiPartUpload(path, filename, partCount, contentType);

        return Resps.json(httpRequest, result);
    }

    /**
     * 完成上传
     *
     * @return /
     */
    @RequestPath("/multipart/complete")
    public HttpResponse completeMultiPartUpload(HttpRequest request) {
        FileMerge file = JSONObject.parseObject(request.getBodyString(), FileMerge.class);
        // 文件名完整路径
        String objectName = file.getObjectName();
        String uploadId = file.getUploadId();
        Assert.notNull(objectName, "objectName must not be null");
        Assert.notNull(uploadId, "uploadId must not be null");
        boolean result = UploadService.mergeMultipartUpload(objectName, uploadId);

        return Resps.txt(request, String.valueOf(result));
    }

    @RequestPath("/download/{location}/{name}")
    public HttpResponse download(String location, String name, HttpRequest request) {
        InputStream inputStream = UploadService.downloadGetStream(location + "/" + name);
        try {
            return Resps.bytes(request, inputStream.readAllBytes(), Im.CHARSET);
        } catch (IOException e) {
            return null;
        }
    }

}
