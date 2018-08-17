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

package com.tuzhihai.file;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author: zhihai.tu
 * Create in  2018/8/15
 */
@Slf4j
public class FileUtil {

    public static final String RN = "\r\n";

    /**
     * @Title: readFilePathList
     * @param @param filepath 文件路径
     * @param @return   返回当前路径下所有文件路径集合
     * @return List<String>
     * @throws IOException
     */
    public static List<String> findFilesInPath(String filepath) throws IOException {
        List<String> list = new LinkedList<String>();
        File file = new File(filepath);
        if (file.exists() && file.isFile()){
            list.add(file.getPath());
            return list;
        }
        findFiles(filepath,list);
        return list;
    }

    public static void findFiles(String filepath,List<String> list){
        File file = new File(filepath);
        if (file.exists() && file.isDirectory()) {
            for (File fileTemp : file.listFiles()){
                if (fileTemp.isFile()){
                    list.add(fileTemp.getPath());
                }
                if (fileTemp.isDirectory()){
                    findFiles(fileTemp.getPath(),list);
                }
            }
        }
    }

    /**
     * @Title: randomAccessWrite
     * @Description: 随机流写操作
     * @param @param filePath 文件路径
     * @param @param content  追加的内容
     * @return void
     * @throws IOException
     */
    public static void randomAccessWrite(String filePath, String content) {
        File fileName = new File(filePath);
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            // byte[] buffer = new byte[1024];
            randomFile.seek(fileLength);
            randomFile.write(content.getBytes());
            log.debug("additional content is complete");
        } catch (IOException e) {
            log.error("randomAccessWrite have a error",e);
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    log.error("randomAccessWrite close have a error",e);
                }
            }
        }
    }

    /**
     * @Title: findLines
     * @Description: TODO
     * @param @param filePath 文件路径
     * @param @param context 查找的字符串
     * @param @return 包含搜索的字符串的行
     * @param @throws IOException
     * @return List<String>
     */
    public static List<String> findLines(String filePath, String context) throws IOException {
        List<String> list = new LinkedList<String>();
        BufferedReader read = new BufferedReader(new FileReader(filePath));
        String str = null;
        while ((str = read.readLine()) != null) {
            if (str.indexOf(context) != -1) {
                list.add(str);
            }
        }
        read.close();
        return list;
    }

    /**
     * @Title: findParagraph
     * @Description: 根据查找字符串划分内容（第一个符合的字符串对象有效） 
     * @param @param filePath 文件路径 
     * @param @param context 查找的字符串 
     * @param @return List<String> 
     * @param @throws IOException    
     * @return List<String>    
     * @throws
     */
    public static List<String> findParagraph(String filePath, String context) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(filePath));
        List<String> list = new LinkedList<String>();
        String paragraphHead = "";
        String paragraphEnd = "";
        String line = "";
        int index = 0;
        int lineNum=1;
        while ((line = read.readLine()) != null) {
            if (index == 0) {
                paragraphHead += (line + RN);
            }
            if (line.indexOf(context) != -1 && index == 0) {
                log.debug("行号："+lineNum+"，当前行内容: "+line);
                list.add(paragraphHead);
                index++;
                continue;
            }
            if (index > 0) {
                paragraphEnd += (line + RN);
            }
            lineNum++;
        }
        list.add(paragraphEnd);
        read.close();
        return list;
    }

    /**
     * @Title: writeFile
     * @Description: TODO
     * @param @param filePath 文件路径名称
     * @param @param context要写入的文件内容
     * @param @param codeType编码格式（默认为utf-8）
     * @param @throws IOException
     * @return void
     * @throws IOException
     */
    public static void writeFile(String filePath, String context, String codeType) throws IOException {
        File f = new File(filePath);
        InputStreamReader read = null;
        if (!StringUtils.isEmpty(codeType)) {
            read = new InputStreamReader(new FileInputStream(f), codeType);
        } else {
            read = new InputStreamReader(new FileInputStream(f), "UTF-8");
        }
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        String str = "";
        while ((line = reader.readLine()) != null) {
            str += (line + RN);
        }
        OutputStream out = new FileOutputStream(f);
        byte[] bt = context.getBytes();
        out.write(bt);
        out.flush();
        out.close();
        log.debug("读取文件结束！" + RN + "开始向文件开始追加内容" + RN + str);
        randomAccessWrite(filePath, str);

    }

    /**
     * 读取文件内容
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        String result = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                result += (line + RN);
            }
        } catch (FileNotFoundException e) {
            log.error("file have not found,filepath:{}",filePath,e);
        }
        return result;
    }
    /**
     * @Title: writeParagraph
     * @Description: TODO
     * @param @param filePath 路径 
     * @param @param context 要查找的字符串 
     * @param @param wcontext要写入的内容 
     * @param @param codeType 编码格式（默认utf-8） 
     * @param @throws IOException    
     * @return void
     * @throws
     */
    public static void writeParagraph(String filePath, String context, String wcontext, String codeType) throws IOException {
        File fileName = new File(filePath);
        List<String> list = findParagraph(filePath, context);
        RandomAccessFile randomFile = null;
        OutputStreamWriter write = null;
        if (codeType != null && !codeType.trim().equals("")) {
            write = new OutputStreamWriter(new FileOutputStream(fileName), codeType);
        } else {
            write = new OutputStreamWriter(new FileOutputStream(fileName), "utf-8");
        }
        //清空文件内容  
        write.write("");
        write.close();
        try {
            // 打开一个随机访问文件流，按读写方式  
            randomFile = new RandomAccessFile(fileName, "rw");
            int index=0;
            for (int i = 0; i < list.size(); i++) {
                if (index==0) {
                    randomFile.write((wcontext + RN).getBytes());
                    randomFile.write(list.get(i).getBytes());
                }
                if (index>0) {
                    randomFile.write(list.get(i).getBytes());
                }
                index++;
            }
            log.info("文件已经完成操作,name:{}",fileName.getName());
        } catch (IOException e) {
            log.error("writeParagraph have a error",e);
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    log.error("writeParagraph close have a error",e);
                }
            }
        }

    }

    /**
     * 清空文件
     * @param path
     */
    public static void clearFile(String path){
        File file = new File(path);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.info("file create fail");
        }
    }

    /**
     * 清除文件第一行（空行）
     * @param path
     * @throws IOException
     */
    public static void clearFileFirstLine(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //读取第一行
        String line = bufferedReader.readLine();
        String str = "";
        if (!StringUtils.isEmpty(line)) {
            str += (line + RN);
        }
        while ((line = bufferedReader.readLine()) != null) {
            str += (line + RN);
        }
        clearFile(path);
        writeFile(path,str,"");
    }

    /**
     * 清除路径下的所有文件第一行（若为空）
     * @param path
     * @throws IOException
     */
    public static void clearFileFirstLineInPath(String path) throws IOException {
        List<String> list = findFilesInPath(path);
        for (String filePath : list){
            clearFileFirstLine(filePath);
        }
    }

    /**
     * 给路径下的所有文件写入内容
     * @param path
     * @param context
     * @param contentPath
     * @throws IOException
     */
    public static void writeInPath(String path,String context,String contentPath) throws IOException {
        String content = "";
        if(new File(contentPath).isFile()){
            content = readFile(contentPath);
        }else {
            content = contentPath;
        }
        List<String> list = findFilesInPath(path);
        for (String filePath : list){
            try {
                writeParagraph(filePath,context,content,null);
            } catch (IOException e) {
                log.error("WriteInPath have a error",e);
            }
        }
    }

    /**
     * 替换路径下的所有文件内容
     * @param path
     * @param oldChar
     * @param newChar
     * @throws IOException
     */
    public static void replaceInPath(String path,String oldChar,String newChar) throws IOException {
        List<String> list = findFilesInPath(path);
        for (String filePath : list){
            String sourceFileContent = readFile(filePath);
            String oldCharTemp = "";
            if(new File(oldChar).isFile()){
                oldCharTemp = readFile(oldChar);
            }else {
                oldCharTemp = oldChar;
            }

            String newCharTemp = "";
            if(new File(newChar).isFile()){
                newCharTemp = readFile(newChar);
            }else {
                newCharTemp = newChar;
            }

            String newContent = sourceFileContent.replace(oldCharTemp,newCharTemp);
            clearFile(filePath);
            writeFile(filePath,newContent,"");
        }

    }


    public static void main(String[] args) throws IOException {
        String context = "package";
        String filePath = "D:\\soft_workspace\\IDEA\\self\\FileUtil\\src";
        String contentFilePath = "D:\\license";

        // 给文件写入内容
        writeInPath(filePath,context,contentFilePath);

        // 替换文件内容
        //replaceInPath(filePath,contentFilePath,"");

        // 清除文件第一行
        //clearFileFirstLineInPath(filePath);
        
    }
}
