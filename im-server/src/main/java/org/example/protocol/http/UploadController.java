package org.example.protocol.http;

import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import org.example.packets.file.Chunk;
import org.example.packets.file.FileInfo;
import org.example.util.FileUtil;
import org.tio.http.common.*;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequestPath("/upload")
public class UploadController {

    private String fileLoadPath = "./files";

    @RequestPath("/chunk")
    public HttpResponse uploadChunk(Chunk chunk, HttpRequest request) {
        // get请求是验证请求
        if (request.requestLine.method.equals(Method.GET)) {
            log.info("文件校验");
            HttpResponse response = Resps.txt(request,"OK");
            if(!Im.get().messageHelper.getChunk(chunk)){
                response.setStatus(HttpResponseStatus.C407);
            }
            return response;
        }
        UploadFile file = chunk.getFile();
        String url = FileUtil.generatePath(fileLoadPath, chunk);
        Path path = Paths.get(url);
        try {
            Files.write(path, file.getData());
            log.debug("文件 {} 写入成功, uuid:{}", chunk.getFilename(), chunk.getIdentifier());
            Im.get().messageHelper.saveChunk(chunk);
        } catch (Exception e) {
            return Resps.txt(request, "后端异常");
        }
        return Resps.txt(request, "文件上传成功");
    }

    @RequestPath("/merge/file")
    public String mergeFile(FileInfo fileInfo) {
        String filename = fileInfo.getFilename();
        String file = fileLoadPath + "/" + fileInfo.getIdentifier() + "/" + filename;
        String folder = fileLoadPath + "/" + fileInfo.getIdentifier();
        FileUtil.merge(file, folder, filename);
        fileInfo.setLocation(file);
        Im.get().messageHelper.saveFileInfo(fileInfo);
        return "合并成功";
    }

}
