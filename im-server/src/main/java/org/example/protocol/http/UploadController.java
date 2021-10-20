package org.example.protocol.http;

import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import org.example.packets.file.Chunk;
import org.example.util.FileUtil;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.UploadFile;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequestPath("/upload")
public class UploadController {

    @RequestPath("/chunk")
    public HttpResponse uploadChunk(Chunk chunk, HttpRequest request){
        UploadFile file = chunk.getFile();
        String url = FileUtil.generatePath("./files", chunk);
        Path path = Paths.get(url);
        try{
            Files.write(path, file.getData());
            log.debug("文件 {} 写入成功, uuid:{}", chunk.getFilename(), chunk.getIdentifier());
            Im.get().messageHelper.saveChunk(chunk);
        }catch (Exception e){
            return Resps.txt(request, "后端异常");
        }

        return Resps.txt(request, "文件上传成功");
    }

    @RequestPath("/check/chunk")
    public HttpResponse checkChunk(Chunk chunk, HttpRequest request){
        return Resps.txt(request, "Get请求");
    }

}
