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

package com.tuzhihai.file.handlers.ftp;


import java.io.*;
import java.util.List;

import com.tuzhihai.file.handlers.FileHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
@Slf4j
public class FtpFileHandler implements FileHandler {


    private FTPClient ftp;

    private String downloadPath;

    private String rootPath;

    public FtpFileHandler(String host, int port, String user, String pwd, String downloadPath, String rootPath) throws Exception {
        ftp = new FTPClient();
        int reply;
        ftp.connect(host, port);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("connect to FTP Server failed");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        this.downloadPath = downloadPath;
        this.rootPath = rootPath;
    }

    @Override
    public boolean write(String filePath, InputStream inputStream) {
        try {
            this.ftp.storeFile(filePath, inputStream);
            return true;
        } catch (IOException e) {
            log.error("upload to remote ftp server failed");
        }
        return false;
    }

    @Override
    public File read(String remotePath) {
        File file = new File(downloadPath);
        try {
            OutputStream out = new FileOutputStream(file);
            if (ftp.retrieveFile(remotePath, out)) {
                return file;
            }
        } catch (IOException e) {
            log.error("download remote file failed");
        }
        return null;
    }

    @Override
    public List<File> listDir(String dir) {
//        try {
//            FTPFile[] ftpFiles = this.ftp.listDirectories();
//            return null;
//        } catch (IOException e) {
//            log.error("list remote dir failed");
//            return null;
//        }
        return null;
    }

    @Override
    public boolean copy(String from, String to) {
        return false;
    }

    @Override
    public boolean move(String from, String to) {
        return false;
    }

    @Override
    public boolean remove(String filePath) {
        return false;
    }
}
