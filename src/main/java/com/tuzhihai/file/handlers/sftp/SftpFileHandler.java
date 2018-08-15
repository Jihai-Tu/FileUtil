package com.tuzhihai.file.handlers.sftp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import com.jcraft.jsch.*;
import com.tuzhihai.file.handlers.FileHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
@Slf4j
public class SftpFileHandler implements FileHandler {

    private ChannelSftp sftp;

    private String downloadPath;

    private String rootPath;

    public SftpFileHandler(String host, int port, String user, String pwd, String downloadPath, String rootPath) throws Exception {
        JSch jsch = new JSch();
        jsch.getSession(user, host, port);
        Session sshSession = jsch.getSession(user, host, port);
        sshSession.setPassword(pwd);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        log.info("SFTP Session connected.");
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        log.info("Connected to " + host);
        this.downloadPath = downloadPath;
        this.rootPath = rootPath;
    }

    @Override
    public boolean write(String filePath, InputStream inputStream) {
        assert !new File(filePath).isFile() : "the file path type must a file";
        try {
            Path writeFilePath = Paths.get(filePath);
            cd(writeFilePath.getParent().toString());
            sftp.put(inputStream, writeFilePath.getFileName().toString());
            inputStream.close();
            disconnect();
            return true;
        } catch (Exception e) {
            log.error("write file[" + filePath + "] failed", e);
            return false;
        }
    }

    @Override
    public File read(String filePath) {
        try {
            Path readFilePath = Paths.get(rootPath, filePath);
            sftp.cd(readFilePath.getParent().toString());
            File file = Paths.get(downloadPath).resolve(readFilePath.getFileName()).toFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(readFilePath.getFileName().toString(), fileOutputStream);
            fileOutputStream.close();
            disconnect();
            return file;
        } catch (Exception e) {
            log.error("read file[" + filePath + "] error", e);
            return null;
        }
    }

    private void disconnect() {
        try {
            sftp.getSession().disconnect();
        } catch (JSchException e) {
            log.error("quit session failed");
        }
        sftp.quit();
        sftp.disconnect();
    }

    @Override
    public List<File> listDir(String dir) {
//        try {
//            List<File> files = (List<File>) sftp.ls(dir).stream().map(i -> new File(((ChannelSftp.LsEntry) i).getLongname()));
//            return files;
//        } catch (SftpException e) {
//            log.error("list fail");
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
        try {
            sftp.rm(filePath);
        } catch (SftpException e) {
            log.error("remove file failed");
        }
        return false;
    }

    /**
     * 创建一个文件目录
     */
    public void cd(String createpath) throws Exception{
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
                return;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    sftp.mkdir(filePath.toString());
                    sftp.cd(filePath.toString());
                }
            }
            this.sftp.cd(createpath);
        } catch (SftpException e) {
            log.error("CD路径错误["+createpath+"]");
            throw new Exception("CD路径错误：" + createpath);
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

}