package com.tuzhihai.file.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhihai.tu
 * Create in  2018/8/6
 */
public class CoreConfig {

    public static Path getHome(){
        return Paths.get(System.getProperty("user.dir"));
    }

}
