package com.tuzhihai.file.bean;

/**
 * @Author: zhihai.tu
 * Create in  2018/8/7
 */
public class SftpFileRequest implements FileRequest{
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

    public SftpFileRequest() {
    }

    public SftpFileRequest(String host, int port, String user, String pwd, String downloadPath, String rootPath) {
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
