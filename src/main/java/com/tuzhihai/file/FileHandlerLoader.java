package com.tuzhihai.file;


import java.util.HashMap;
import java.util.Map;

import com.tuzhihai.file.bean.FileRequest;
import com.tuzhihai.file.enums.FilePathTypeEnum;
import com.tuzhihai.file.handlers.FileHandler;
import com.tuzhihai.file.handlers.FileHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author: zhihai.tu
 * @create: 2018/8/6
 */
@Component
public class FileHandlerLoader implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(FileHandlerLoader.class);

    private ApplicationContext applicationContext;

    private Map<String, FileHandlerFactory<?>> factoryMap;

    public FileHandler load(FilePathTypeEnum pathType, FileRequest fileRequest) throws Exception {
        FileHandlerFactory<?> fileHandlerFactory = factoryMap.get(pathType.getType());
        if (fileHandlerFactory == null) {
            throw new Exception("handler[" + pathType.getType() +"]init failed ");
        }
        return fileHandlerFactory.build(fileRequest);
    }

    @Override
    public void afterPropertiesSet() {
        factoryMap = new HashMap<>();
        Map<String,FileHandlerFactory> map = getBeanList(applicationContext, FileHandlerFactory.class);
        logger.info("file handler factory map [{}]", map);
        map.entrySet().forEach(entry->{
            factoryMap.put(StringUtils.uncapitalize(entry.getValue().getClass().getSimpleName().replace("FileHandlerFactory",
                    "")), entry.getValue());
        });
        logger.info("file handler factory map [{}]", factoryMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> Map getBeanList(ListableBeanFactory beanFactory, Class<T> clazz){
        Map<String, T> map = beanFactory.getBeansOfType(clazz);
        return map;
    }
}