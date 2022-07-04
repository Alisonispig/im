package org.example.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import org.example.dao.FileRepository;
import org.example.packets.bean.FileInfo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FileService {

    private final FileRepository fileRepository;

    public FileService() {
        fileRepository = new FileRepository();
    }

    public String getFileUrl(String md5, Long size, String type) {
        FileInfo fileInfo = fileRepository.findOne(and(eq(FileInfo.COL_MD5, md5), eq(FileInfo.COL_SIZE, size), eq(FileInfo.COL_TYPE, type)));
        return fileInfo == null ? null : fileInfo.getUrl();
    }

    public String getFileUrl(String md5) {
        FileInfo fileInfo = fileRepository.findOne(eq(FileInfo.COL_MD5, md5));
        return fileInfo == null ? null : fileInfo.getUrl();
    }

    public void setFileUrl(String md5, String objectName, String name, Long size) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(IdUtil.getSnowflake().nextIdStr());
        fileInfo.setMd5(md5);
        fileInfo.setUrl(objectName);
        fileInfo.setName(name);
        fileInfo.setSize(size);
        fileInfo.setType(FileNameUtil.getSuffix(name));
        fileRepository.saveOrUpdateById(fileInfo);
    }
}
