package com.tuzhihai.file.handlers.ftp;


import com.tuzhihai.file.bean.FileRequest;
import com.tuzhihai.file.bean.FtpFileRequest;
import com.tuzhihai.file.handlers.FileHandlerFactory;
import com.tuzhihai.file.util.CoreConfig;
import org.springframework.stereotype.Component;


/**
 * @author: zhihai.tu
 * @create: 2018/8/6
 */
@Component
public class FtpFileHandlerFactory implements FileHandlerFactory<FtpFileHandler> {

    @Override
    public FtpFileHandler build(FileRequest fileRequest) throws Exception {
        FtpFileRequest ftpFileRequest = (FtpFileRequest) fileRequest;
        String downloadPath  = CoreConfig.getHome().resolve(ftpFileRequest.getDownloadPath()).toString();
        return new FtpFileHandler(ftpFileRequest.getHost(), ftpFileRequest.getPort(), ftpFileRequest.getUser(),
                ftpFileRequest.getPwd(), downloadPath, ftpFileRequest.getRootPath());
    }
}