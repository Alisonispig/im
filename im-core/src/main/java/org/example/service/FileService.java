package org.example.service;

import org.example.dao.FileRepository;
import org.example.packets.bean.FileInfo;

public class FileService {

    private final FileRepository fileRepository;

    public FileService() {
        fileRepository = new FileRepository();
    }

    public String getFileUrl(String md5) {
        FileInfo fileInfo = fileRepository.findById(md5);
        return fileInfo == null ? null : fileInfo.getUrl();
    }

    public void setFileUrl(String md5, String objectName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMd5(md5);
        fileInfo.setUrl(objectName);
        fileRepository.saveOrUpdateById(fileInfo);
    }
}
