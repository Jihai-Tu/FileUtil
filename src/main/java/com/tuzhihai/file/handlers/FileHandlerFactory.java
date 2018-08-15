package com.tuzhihai.file.handlers;

import com.tuzhihai.file.bean.FileRequest;

/**
 * @author: zhihai.tu
 * create: 2018/8/6
 */
public interface FileHandlerFactory<T extends FileHandler> {
    T build(FileRequest fileRequest) throws Exception;
}