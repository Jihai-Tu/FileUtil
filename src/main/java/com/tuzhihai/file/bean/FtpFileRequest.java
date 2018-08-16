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

package com.tuzhihai.file.bean;

/**
 * @author: zhihai.tu
 * Create in  2018/8/7
 */
public class FtpFileRequest implements FileRequest{

    private String host;
    private int port;
    private String user;
    private String pwd;
    private String downloadPath;
    private String rootPath;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public FtpFileRequest() {
    }

    public FtpFileRequest(String host, int port, String user, String pwd, String downloadPath, String rootPath) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
        this.downloadPath = downloadPath;
        this.rootPath = rootPath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileRequest{");
        sb.append(", host='").append(host).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", user='").append(user).append('\'');
        sb.append(", pwd='").append(pwd).append('\'');
        sb.append(", downloadPath='").append(downloadPath).append('\'');
        sb.append(", rootPath='").append(rootPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
