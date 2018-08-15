package com.tuzhihai.file.handlers.local;


import java.nio.file.Path;
import java.nio.file.Paths;

import com.tuzhihai.file.bean.FileRequest;
import com.tuzhihai.file.bean.LocalFileRequest;
import com.tuzhihai.file.handlers.FileHandler;
import com.tuzhihai.file.handlers.FileHandlerFactory;
import com.tuzhihai.file.util.CoreConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author: zhihai.tu
 * create: 2018/6/28
 */
@Component
public class LocalFileHandlerFactory implements FileHandlerFactory {
    private Path basePath;

    @Override
    public FileHandler build(FileRequest fileRequest) throws Exception {
        LocalFileRequest request = (LocalFileRequest) fileRequest;
        String basePathString = request.getBasePath();
        if (StringUtils.isEmpty(basePathString)) {
            basePath = CoreConfig.getHome();
        } else {
            basePath = Paths.get(basePathString);
        }
        return new LocalFileHandler(basePath);
    }
}