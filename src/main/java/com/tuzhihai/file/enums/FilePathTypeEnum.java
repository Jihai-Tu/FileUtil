package com.tuzhihai.file.enums;

public enum FilePathTypeEnum {
    FILE_PATH_TYPE_FTP("ftp","FTP"),
    FILE_PATH_TYPE_SFTP("sFtp","SFTP"),
    FILE_PATH_TYPE_LOCAL("local","本地存储"),
    ;

    private String type;
    private String desc;

    FilePathTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "FilePathTypeEnum{" +
                "type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }


}
