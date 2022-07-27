package org.example.protocol.http;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.config.CourierConfig;
import org.example.dao.EmoticonRepository;
import org.example.packets.bean.Emoticon;
import org.example.packets.file.FileInit;
import org.example.packets.file.FileMerge;
import org.example.protocol.http.service.UploadService;
import org.example.service.FileService;
import org.example.util.ThreadPoolUtil;
import org.tio.http.common.*;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestPath("/file")
public class UploadController {

    private final FileService fileService;

    private final EmoticonRepository emoticonRepository;

    public UploadController() {
        fileService = new FileService();
        emoticonRepository =  new EmoticonRepository();
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

        Long size = ObjectUtil.defaultIfNull(fileInit.getSize(), 0L);

        String url = fileService.getFileUrl(md5, size, FileNameUtil.getSuffix(filename));

        if (StrUtil.isNotBlank(url) && CourierConfig.checkFileMd5) {
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
        Long size = file.getSize();
        String name = file.getName();
        Assert.notNull(objectName, "objectName must not be null");
        Assert.notNull(md5, "md5 must not be null");

        if (StrUtil.isNotBlank(uploadId)) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> UploadService.mergeMultipartUpload(objectName, uploadId), ThreadPoolUtil.commonPool);

            future.thenAccept((item) -> {
                if (item) {
                    fileService.setFileUrl(md5, objectName, name, size);
                }
            });
        } else {
            fileService.setFileUrl(md5, objectName, name, size);
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

    @RequestPath("/upload/emoticon")
    public HttpResponse uploadEmoticon(UploadFile uploadFile,String before, String end, HttpRequest request) {
        String suffix = FileNameUtil.getSuffix(uploadFile.getName());
        String s = IdUtil.getSnowflake().nextIdStr() + (StrUtil.isNotBlank(suffix) ? CharUtil.DOT : "") + suffix;

        boolean b = UploadService.uploadFile(uploadFile.getData(), s);

        Emoticon emoticon = new Emoticon();
        emoticon.setId(IdUtil.getSnowflakeNextIdStr());
        emoticon.setName(uploadFile.getName());
        emoticon.setSize((long) uploadFile.getSize());
        emoticon.setIsPrivate(false);
        emoticon.setType(suffix);
        emoticon.setUrl(CourierConfig.fileUrl + s);

        emoticonRepository.insert(emoticon);

        return Resps.txt(request, "OK");
    }

}
