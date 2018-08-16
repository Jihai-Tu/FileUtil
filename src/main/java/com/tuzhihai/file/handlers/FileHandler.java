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

package com.tuzhihai.file.handlers;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
public interface FileHandler {
    /**
     * 文件写入
     *
     * @param filePath    写入文件路径
     * @param inputStream 写入文件流
     * @return true-成功 false-失败
     */
    boolean write(String filePath, InputStream inputStream);

    /**
     * 文件读取
     *
     * @param filePath 文件路径
     * @return 文件对象
     */
    File read(String filePath);

    /**
     * 读取目录下的所有文件
     *
     * @param dir 文件目录
     * @return 文件对象列表
     */
    List<File> listDir(String dir);

    /**
     * 复制文件
     *
     * @param from 来源文件
     * @param to   目标文件
     * @return true-复制成功 false-复制失败
     */
    boolean copy(String from, String to);

    /**
     * 移动文件
     *
     * @param from 来源文件
     * @param to   目标文件
     * @return true-复制成功 false-复制失败
     */
    boolean move(String from, String to);

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return true-删除成功 false-删除失败
     */
    boolean remove(String filePath);
}
