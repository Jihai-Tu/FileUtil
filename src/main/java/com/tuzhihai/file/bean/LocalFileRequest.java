package com.tuzhihai.file.bean;

/**
 * @Author: zhihai.tu
 * Create in  2018/8/7
 */
public class LocalFileRequest implements FileRequest {
    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public LocalFileRequest() {
    }

    public LocalFileRequest(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileLocalRequest{");
        sb.append("basePath='").append(basePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
