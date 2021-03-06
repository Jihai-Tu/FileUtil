/*
 * Copyright (c) 2018-2025 tuzhihai(涂志海) All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
