package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Code;
import com.util.FileUtils;
import com.util.TextUtil;

public class GetFileNameFromPath {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";

    public Map<String, List<File>> imgFolders = new HashMap<String, List<File>>();

    /* Properties util */
    Properties prop;

    /* Program location */
    private final String path = FileUtils.getPath();

    public String serviceId;

    Logger logger = null;

    public GetFileNameFromPath() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("処理開始");
        try {
            FileReader reader = new FileReader(path + "//" + PROP_FILE);
            prop = new Properties();
            prop.load(reader);

            serviceId = prop.getProperty("serviceId");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        GetFileNameFromPath thisClass = new GetFileNameFromPath();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入路径");
        String path = scanner.nextLine();
        thisClass.doService(path);
    }

    public void doService(String javaPath) {

        if (!TextUtil.isNotEmpty(javaPath)) {
            javaPath = "C:\\Users\\dylsw\\OneDrive\\图片\\gif";
        }

        BufferedWriter resultFile = FileUtils.getWriter(path + "//filenames.json");

        StringBuilder sb = new StringBuilder();

        imgFolders = new HashMap<String, List<File>>();
        getImageFrombaseFolder(javaPath);

        System.out.println(creatJson(javaPath, resultFile));
        logger.info("処理終了");

        FileUtils.writeFileAndPrintConsole(sb.toString(), resultFile);
        FileUtils.closeWriteSteam(resultFile);
    }

    private void getImageFrombaseFolder(String path) {
        File folder = new File(path);
        for (File child : folder.listFiles()) {
            if (child.isDirectory()) {
                getImageFrombaseFolder(child.getAbsolutePath());
            } else {
                if (!TextUtil.isInclude(new String[] { "Thumbs.db", "desktop.ini" }, child.getName())) {
                    getFilesName(folder.getAbsolutePath());
                    break;
                }
            }
        }
    }

    private String getFilesName(String path) {
        StringBuilder sb = new StringBuilder();
        File folder = new File(path);
        if (null == folder || !folder.isDirectory()) {
            sb.append(path + "がありません！" + Code.WINDOWS_ENTRY);
        } else {
            File[] files = folder.listFiles();
            if (null == files || files.length == 0) {
                sb.append(path + "にファイルがありません！" + Code.WINDOWS_ENTRY);
            } else {
                List<File> imgList = new ArrayList<File>();
                for (File file : files) {
                    imgList.add(file);
                    sb.append(file.getAbsolutePath() + Code.WINDOWS_ENTRY);
                }
                imgFolders.put(folder.getName(), imgList);
            }
        }

        return sb.toString();
    }

    private String creatJson(String basePath, BufferedWriter resultFile) {
        StringBuilder sb = new StringBuilder();
        Set<String> folderMap = imgFolders.keySet();

        sb.append("var jsonData = {" + Code.WINDOWS_ENTRY);
        sb.append("    \"basePath\":\"" + basePath.replace("\\", "\\\\") + "\" ," + Code.WINDOWS_ENTRY);
        sb.append("    \"album\" : [" + Code.WINDOWS_ENTRY);
        int count = 0;
        for (String folder : folderMap) {
            // 内容开始
            sb.append(Code.SPACE_8 + "{\"path\" : \"" + folder + "\" , " + Code.WINDOWS_ENTRY);
            sb.append(Code.SPACE_8 + "  \"images\" : [" + Code.WINDOWS_ENTRY);
            List<File> images = imgFolders.get(folder);
            for (int i = 0; i < images.size(); i++) {
                sb.append(Code.SPACE_12 + "\"" + images.get(i).toString().replace("\\", "\\\\") + "\"");
                if (i < images.size() - 1) {
                    sb.append(",");
                }
                sb.append(Code.WINDOWS_ENTRY);
            }
            // 内容结束
            sb.append(Code.SPACE_12 + "]" + Code.WINDOWS_ENTRY);
            sb.append(Code.SPACE_8 + "}");
            if (count < folderMap.size() - 1) {
                sb.append(",");
            }
            sb.append(Code.WINDOWS_ENTRY);
            count++;
        }
        sb.append(Code.SPACE_4 + "]" + Code.WINDOWS_ENTRY);
        sb.append("}");
        FileUtils.writeFileAndPrintConsole(sb.toString(), resultFile);
        return sb.toString();
    }
}