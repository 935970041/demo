package com.example.demo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     *
     * @Description:遍历该路径下的所有文件
     * @param fileDir
     * @throws Exception
     * @Author:ZhangZhiyuan
     * @Since:2018年1月25日 下午2:09:13
     */
    public static void ergodicFile(String fileDir) {
        List<File> fileList = new ArrayList<>();
        File file = new File(fileDir);
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("为空");
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) { // Directory 目录
                ergodicFile(f.getAbsolutePath());
            } else if (f.isFile()) { // File 文件
                fileList.add(f);
            }
        }
        for (File fileOut : fileList) {
            System.out.println(fileOut.getName());
        }
    }

}
