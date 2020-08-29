package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Code;
import com.util.FileUtils;

public class GetFileNameFromPath {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";

    /* Properties util */
    Properties prop;

    /* Program location */
    private final String path = FileUtils.getPath();
    
    private String serviceId;

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

    public static void main(String[] args) {
        GetFileNameFromPath thisClass = new GetFileNameFromPath();

        thisClass.doService();
    }

	public void doService() {
		BufferedWriter resultFile = FileUtils.getWriter(path + "//filenames.txt");

		StringBuilder sb = new StringBuilder();

		String javaPath = "";
		sb.append(getFilesName(javaPath));

		String jspPath = "";
		sb.append(getFilesName(jspPath));

		String docPath = "";
		sb.append(getFilesName(docPath));

		logger.info("処理終了");

		FileUtils.writeFileAndPrintConsole(sb.toString(), resultFile);
		FileUtils.closeWriteSteam(resultFile);
	}
    
    private String getFilesName (String path) {
    	StringBuilder sb = new StringBuilder();
    	File folder = new File(path);
    	if(null == folder || !folder.isDirectory()) {
    		sb.append(path+"がありません！"+Code.WINDOWS_ENTRY);
    	} else {
    		File[] files = folder.listFiles();
    		if (null == files || files.length == 0) {
    			sb.append(path+"にファイルがありません！"+Code.WINDOWS_ENTRY);
    		} else {
    			for(File file : files) {
    				sb.append(file.getName()+file.getAbsolutePath()+Code.WINDOWS_ENTRY);
    			}
    		}
    	}

    	return sb.toString();
    }

}