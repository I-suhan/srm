package com.ne.srm.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class FileUtil {

    public static String fileParser(FileInputStream fis) throws IOException {
        Tika tika = new Tika();
        return tika.detect(fis);
    }
}
