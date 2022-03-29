package com.application.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * 取得文件内容
     *
     * @param  file
     * @return
     */
    public static List<String> getFileText(File file) {
        List<String> fileTxtList = new ArrayList<String>();
        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = br.readLine())) {
                fileTxtList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileTxtList;
    }

    /**
     * 取得写文件流
     *
     * @param  fullpath
     * @return
     */
    public static BufferedWriter getWriter(String fullpath) {
        File writeFile = new File(fullpath);
        BufferedWriter bw = null;
        try {
            if (!writeFile.exists()) {
                writeFile.createNewFile();
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fullpath)), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }

    /**
     * 使用写文件流写入内容
     *
     * @param txt
     * @param bw
     */
    public static void writeFileAndPrintConsole(String txt, BufferedWriter bw) {
        try {
            bw.write(txt + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭流
     *
     * @param bw
     */
    public static void closeWriteSteam(BufferedWriter bw) {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成有内容的文件
     */
    public static void writeFile(String fullpath, String text) {
        BufferedWriter bw = getWriter(fullpath);
        writeFileAndPrintConsole(text, bw);
        closeWriteSteam(bw);
    }

    /**
     * 取得工程路径
     *
     * @return
     */
    public static String getPath() {
        File localFile = new File("");
        return localFile.getAbsolutePath();
    }

    /**
     * 创建文件夹
     *
     * @param srcPkg
     * @param basePath
     */
    public static void createFolder(String srcPkg, String basePath) {
        String[] folders = null;
        if (srcPkg.indexOf(".") > 0) {
            folders = srcPkg.split("\\.");
        } else if (srcPkg.indexOf("\\") > 0) {
            folders = srcPkg.split("\\\\");
        }

        if (null == folders || folders.length == 0) {
            return;
        } else {
            basePath = TextUtil.isNotEmpty(basePath) ? basePath : FileUtils.getPath();
            for (String folder : folders) {
                basePath += "\\" + folder;
                File fd = new File(basePath);
                if (!fd.exists()) {
                    fd.mkdir();
                }
            }
        }
    }
}
