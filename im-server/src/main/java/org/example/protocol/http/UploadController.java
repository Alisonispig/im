package org.example.protocol.http;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import org.example.packets.file.FileInit;
import org.example.packets.file.FileMerge;
import org.example.protocol.http.service.UploadService;
import org.example.service.FileService;
import org.example.util.ThreadPoolUtil;
import org.tio.http.common.HeaderName;
import org.tio.http.common.HeaderValue;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestPath
public class UploadController {

    private final FileService fileService;

    public UploadController() {
        fileService = new FileService();
    }

    @RequestPath("/multipart/init")
    public HttpResponse initMultiPartUpload(HttpRequest httpRequest) {
        FileInit fileInit = JSONObject.parseObject(httpRequest.getBodyString(), FileInit.class);
        // 文件名
        String filename = ObjectUtil.defaultIfNull(fileInit.getFilename(), "");
        // content-type
        String contentType = ObjectUtil.defaultIfNull(fileInit.getContentType(), "application/octet-stream");
        // md5-可进行秒传判断
        String md5 = ObjectUtil.defaultIfNull(fileInit.getMd5(), "");

        String url = fileService.getFileUrl(md5);

        if (StrUtil.isNotBlank(url) && Im.checkFileMd5) {
            Map<String, Object> su = new HashMap<>();
            su.put("objectName", url);
            su.put("quick", true);
            return Resps.json(httpRequest, su);
        }
        // 分片数量
        Integer partCount = ObjectUtil.defaultIfNull(fileInit.getPartCount(), 1);

        Map<String, Object> result = UploadService.initMultiPartUpload(filename, partCount, contentType);

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
        String md5 = file.getMd5();
        Assert.notNull(objectName, "objectName must not be null");
        Assert.notNull(md5, "md5 must not be null");

        if (StrUtil.isNotBlank(uploadId)) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> UploadService.mergeMultipartUpload(objectName, uploadId),
                    ThreadPoolUtil.commonPool);

            future.thenAccept((item) -> {
                if (item) {
                    System.out.println(item);
                    fileService.setFileUrl(md5, objectName);
                }
            });
        } else {
            fileService.setFileUrl(md5, objectName);
        }

        return Resps.txt(request, "true");
    }

    @RequestPath("/download/{name}")
    public HttpResponse download(String name, HttpRequest request) throws Exception {
        InputStream inputStream = UploadService.downloadGetStream(name);
        if (inputStream == null) {
            return Resps.resp404(request);
        }
        try {
            HttpResponse response = Resps.bytes(request, inputStream.readAllBytes(), FileNameUtil.extName(name));
            response.setHasGzipped(true);
            return response;
        } catch (IOException e) {
            return null;
        }
    }

}
