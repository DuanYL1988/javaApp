package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCope {

    private static final String BASE_SRC = "D:\\搜狗高速下载\\tony\\Tony\\Tony [T2 ART WORKS (Tony)]\\ArtWorks";
    private static final File BASE_FOLDER = new File(BASE_SRC);

    public static void main(String[] args) {
        FileCope local = new FileCope();
        local.loopFolder(BASE_FOLDER);
        System.out.println("Finish");
    }

    private void loopFolder(File file) {
        if (file.isDirectory()) {
            String[] childs = file.list();
            for (String childPath : childs) {
                File childFile = new File(file.getAbsolutePath() + "\\" + childPath);
                loopFolder(childFile);
            }
        } else {
            String[] fileName = file.getName().split("\\.");
            String[] path = file.getAbsolutePath().split("\\\\");
            String pathName = path[path.length - 2] + "[" + fileName[0] + "]." + fileName[1];
            File dest = new File(BASE_SRC + "\\" + pathName);
            System.out.println(pathName);
            try {
                copyFileUsingFileChannels(file, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("resource")
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
