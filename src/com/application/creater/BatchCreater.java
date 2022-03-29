package com.application.creater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.common.Code;
import com.application.util.FileUtils;
import com.application.util.StringUtils;

public class BatchCreater {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";

    /* Properties util */
    Properties prop;

    /* Program location */
    private final String path = FileUtils.getPath();

    /* shell Id */
    private String shellId;

    /* shell Name */
    private String shellName;

    /* shell Name */
    private String tmpTbl;

    /* shell Name */
    private String tmpTblName;
    
    /* shell Name */
    private String originTbl;
    
    /* shell Name */
    private String originTblName;

    /* shell Name */
    private String csvKbn;

    Logger logger = null;

    public BatchCreater() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("処理開始");
        try {
            FileReader reader = new FileReader(path + "//" + PROP_FILE);
            prop = new Properties();
            prop.load(reader);
            shellId = prop.getProperty("shell_id");
            shellName = prop.getProperty("shell_name");
            tmpTbl = prop.getProperty("tmp_table");
            tmpTblName = prop.getProperty("tmp_table_name");
            originTbl = prop.getProperty("origin_table");
            originTblName = prop.getProperty("origin_table_name");
            try {
                csvKbn = shellId.split("_")[2].substring(0, 2).toUpperCase();
            } catch (Exception e) {
                String msg = StringUtils.createErrorMsg(this.getClass().toString(), "BatchCreater", "property", e.getMessage());
                logger.error(msg);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
    }

    public static void main(String[] args) {
        BatchCreater thisClass = new BatchCreater();

        thisClass.createShell();
        thisClass.createProcedure();
    }


    /**
     * create shell file
     * @return
     */
    public void createShell() {
        logger.info("shell作成開始");
        try {
            BufferedWriter shWriter = FileUtils.getWriter(path + "//" + shellId + ".sh");

            StringBuilder shell = new StringBuilder();
            shell.append(formateTemplate("sample.sh"));
            System.out.println(shell.toString());
            FileUtils.writeFileAndPrintConsole(shell.toString(), shWriter);
            FileUtils.closeWriteSteam(shWriter);
        } catch (Exception e) {
            logger.error(StringUtils.createErrorMsg(this.getClass().toString(), "createShell", "java", e.getMessage()));
        }
        logger.info("shell正常終了,ファイル：" + shellId + ".sh");
    }

    public void createProcedure() {
        logger.info("procedure作成開始正常終了");
        try {
            BufferedWriter procWriter = FileUtils.getWriter(path + "//" + shellId + ".txt");

            StringBuilder procedure = new StringBuilder();
            procedure.append(formateTemplate("procedure.sql"));
            
            FileUtils.writeFileAndPrintConsole(procedure.toString(), procWriter);
            FileUtils.closeWriteSteam(procWriter);
        } catch (Exception e) {
            logger.error(StringUtils.createErrorMsg(this.getClass().toString(), "createShell", "java", e.getMessage()));
        }
        logger.info("procedure正常終了,ファイル：" + shellId + ".txt");
    }

    private String formateTemplate(String templateName) {
        
        boolean exAreaFlag = false;
        boolean ipAreaFlag = false;
        StringBuilder template = new StringBuilder();
        List<String> lines = FileUtils.getFileText(new File(path+ "//" +templateName));
        for (String line : lines) {
            // format
            line = line.replace("#shellId#", shellId);
            line = line.replace("#shellName#", shellName);
            line = line.replace("#tmp_table#", tmpTbl);
            line = line.replace("#tmp_table_name#", tmpTblName);
            line = line.replace("#origin_table#", originTbl);
            line = line.replace("#origin_table_name#", originTblName);

            if (line.indexOf("CSV_EXPORT_START") > 0) {
                exAreaFlag = true;
                continue;
            } else if (line.indexOf("CSV_EXPORT_END") > 0) {
                exAreaFlag = false;
                continue;
            }
            if ("EP".equals(csvKbn) && exAreaFlag) {
                template.append(line+Code.UNIX_ENTRY);
                continue;
            }

            if (line.indexOf("CSV_IMPORT_START") > 0) {
                ipAreaFlag = true;
                continue;
            } else if (line.indexOf("CSV_IMPORT_END") > 0) {
                ipAreaFlag = false;
                continue;
            }
            if ("IP".equals(csvKbn) && ipAreaFlag) {
                template.append(line+Code.UNIX_ENTRY);
                continue;
            }
            
            if (!ipAreaFlag && !exAreaFlag) {
                template.append(line+Code.UNIX_ENTRY);
                continue;
            }

        }
        return template.toString();
    }


}