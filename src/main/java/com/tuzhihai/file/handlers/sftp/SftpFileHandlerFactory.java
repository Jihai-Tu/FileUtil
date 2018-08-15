package com.tuzhihai.file.handlers.sftp;


import java.io.File;

import com.tuzhihai.file.bean.FileRequest;
import com.tuzhihai.file.bean.SftpFileRequest;
import com.tuzhihai.file.handlers.FileHandler;
import com.tuzhihai.file.handlers.FileHandlerFactory;
import com.tuzhihai.file.util.CoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
@Slf4j
@Component
public class SftpFileHandlerFactory implements FileHandlerFactory<FileHandler> {

    @Override
    public FileHandler build(FileRequest fileRequest) throws Exception {
        SftpFileRequest sftpFileRequest = (SftpFileRequest) fileRequest;
        String downloadPath = CoreConfig.getHome().resolve(sftpFileRequest.getDownloadPath()).toString();
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists() && !downloadDir.mkdirs()) {
            throw new Exception("create download dir [" + downloadPath + "] failed");
        }
        return new SftpFileHandler(sftpFileRequest.getHost(), sftpFileRequest.getPort(), sftpFileRequest.getUser(),
                sftpFileRequest.getPwd(), downloadPath, sftpFileRequest.getRootPath());
    }
}