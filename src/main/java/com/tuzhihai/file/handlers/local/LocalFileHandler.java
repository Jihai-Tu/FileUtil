package com.tuzhihai.file.handlers.local;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tuzhihai.file.handlers.FileHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;


/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
@Slf4j
public class LocalFileHandler implements FileHandler {

    private Path basePath;

    public LocalFileHandler(Path basePath){
        this.basePath = basePath;
    }

    @Override
    public boolean write(String filePath, InputStream inputStream) {
        //mkdirs(filePath);
        Path path = basePath.resolve(filePath);
        File file = path.toFile();
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.copyInputStreamToFile(inputStream, path.toFile());
        } catch (IOException e) {
            log.error("写入文件[{}]失败,{}", filePath, e);
            return false;
        }
        return true;
    }

    @Override
    public File read(String filePath) {
        Path path = basePath.resolve(filePath);
        return path.toFile();
    }

    @Override
    public List<File> listDir(String dir) {
        Path path = basePath.resolve(dir);
        File dirFile = path.toFile();
        if (!dirFile.isDirectory()) {
            log.error("读取文件列表[{}]失败", dir);
            return new ArrayList<>();
        }
        Collection<File> files = FileUtils.listFiles(dirFile, new String[]{}, true);
        return new ArrayList<>(files);
    }

    @Override
    public boolean copy(String from, String to) {
        Path fromPath = basePath.resolve(from);
        Path toPath = basePath.resolve(to);

        File fromFile = fromPath.toFile();
        File toFile = toPath.toFile();
        try {
            if (fromFile.isDirectory() && toFile.isDirectory()) {
                FileUtils.copyDirectory(fromFile, toFile);
            } else if (fromFile.isFile() && toFile.isFile()) {
                FileUtils.copyFile(fromFile, toFile);
            } else {
                log.error("复制文件from[{}] to[{}]失败，文件必须同为文件夹或文件对象", from, to);
                return false;
            }
        } catch (IOException e) {
            log.error("复制文件from[{}] to[{}]失败,{}", from, to, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean move(String from, String to) {
        Path fromPath = basePath.resolve(from);
        Path toPath = basePath.resolve(to);

        File fromFile = fromPath.toFile();
        File toFile = toPath.toFile();

        try {
            if (fromFile.isDirectory() && toFile.isDirectory()) {
                FileUtils.moveDirectory(fromFile, toFile);
            } else if (fromFile.isFile() && toFile.isFile()) {
                FileUtils.moveFile(fromFile, toFile);
            } else {
                log.error("复制文件from[{}] to[{}]失败，文件必须同为文件夹或文件对象", from, to);
                return false;
            }
        } catch (IOException e) {
            log.error("复制文件from[{}] to[{}]失败,{}", from, to, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(String filePath) {
        Path removePath = basePath.resolve(filePath);
        File removeFile = removePath.toFile();
        try {
            removeFile.delete();
            FileUtils.forceDeleteOnExit(removeFile);
        } catch (IOException e) {
            log.error("删除文件[{}]失败", filePath);
            return false;
        }
        return true;
    }

    /**
     * 创建一个文件目录
     */
    public static void mkdirs(String createpath){
        createpath = createpath.substring(0,createpath.lastIndexOf("/")+1);
        File file = new File(createpath);
        file.mkdirs();
    }

}