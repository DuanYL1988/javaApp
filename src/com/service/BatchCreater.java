package com.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Code;
import com.util.FileUtils;
import com.util.StringUtils;

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
    private String targetTbl;

    /* shell Name */
    private String targetTblName;

    /* shell Name */
    private String csvExportKbn;

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
            targetTbl = prop.getProperty("target_table");
            targetTblName = prop.getProperty("target_table_name");
            csvExportKbn = prop.getProperty("csv_export_kbn");
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
        boolean csvKbn = "1".equals(csvExportKbn) ? true : false;
        boolean csvAreaFlag = false;
        StringBuilder template = new StringBuilder();
        List<String> lines = FileUtils.getFileText(new File(path+ "//" +templateName));
        for (String line : lines) {
            // format
            line = line.replace("#shellId#", shellId);
            line = line.replace("#shellName#", shellName);
            line = line.replace("#target_table#", targetTbl);
            line = line.replace("#target_table_name#", targetTblName);

            if (line.indexOf("CSV_EXPORT_START")>0) {
                csvAreaFlag = true;
                continue;
            } else if (line.indexOf("CSV_EXPORT_END")>0) {
                csvAreaFlag = false;
                continue;
            } else if (csvKbn || (!csvKbn && !csvAreaFlag)) {
                template.append(line+Code.UNIX_ENTRY);
            }
        }
        return template.toString();
    }

}