package com.service;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.Code;
import com.util.DateTimeUtil;
import com.util.FileUtils;

public class BatchCreater {

    /* config properties file */
    private static final String PROP_FILE = "config.properties";

    /* Properties util */
    Properties prop;

    /* Program location */
    private static String path = FileUtils.getPath();

    /* shell Id */
    private static String shellId;

    /* shell Name */
    private static String shellName;

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
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
    }

    public static void main(String[] args) {
        BatchCreater thisClass = new BatchCreater();

        String shell = thisClass.createShell();
        BufferedWriter shWriter = FileUtils.getWriter(path + "//" + shellId + ".sh");
        FileUtils.writeFileAndPrintConsole(shell.toString(), shWriter);
        FileUtils.closeWriteSteam(shWriter);

        String procedure = thisClass.createProcedure();
        BufferedWriter procWriter = FileUtils.getWriter(path + "//" + shellId + ".txt");
        FileUtils.writeFileAndPrintConsole(procedure.toString(), procWriter);
        FileUtils.closeWriteSteam(procWriter);
    }

    /**
     * create shell file
     * @return
     */
    public String createShell() {
        StringBuilder shell = new StringBuilder();
        // command
        shell.append(createShellCmd());
        //
        shell.append(createShellBody());
        return shell.toString();
    }

    public String createProcedure() {
        StringBuilder procedure = new StringBuilder();
        procedure.append(createProcCmd());

        procedure.append(createProcBody());
        return procedure.toString();
    }

    /**
     * shell command
     */
    private String createShellCmd() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("################################################################"+Code.UNIX_ENTRY);
        cmd.append("# システム名     : 原材料受発注システム"+Code.UNIX_ENTRY);
        cmd.append("# 機能名            : "+shellId+"_"+shellName+Code.UNIX_ENTRY);
        cmd.append("# 作成日            : "+DateTimeUtil.getCurrentDate(DateTimeUtil.YMD)+Code.UNIX_ENTRY);
        cmd.append("# 最終更新日     : -"+Code.UNIX_ENTRY);
        cmd.append("# 最終更新者     : NTTデータスミス"+Code.UNIX_ENTRY);
        cmd.append("# 概要                : "+shellName+Code.UNIX_ENTRY);
        cmd.append("# 引数                : なし"+Code.UNIX_ENTRY);
        cmd.append("# 正常終了        : [0]正常終了"+Code.UNIX_ENTRY);
        cmd.append("# 異常終了        : [90]システムエラー　[91]データ無エラー"+Code.UNIX_ENTRY);
        cmd.append("################################################################"+Code.UNIX_ENTRY);
        cmd.append(Code.UNIX_ENTRY);
        return cmd.toString();
    }

    private String createShellBody() {
        StringBuilder body = new StringBuilder();

        body.append("#共通設定定義を読み込み" + Code.UNIX_ENTRY);
        body.append("source /mnt/efs/job/batch/bin/conf/common.conf" + Code.UNIX_ENTRY);
        body.append("source ${ENV_COMMON_FNC}" + Code.UNIX_ENTRY);
        body.append("export PGUSER" + Code.UNIX_ENTRY);
        body.append("export PGPASSWORD" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#変数定義" + Code.UNIX_ENTRY);
        body.append("#プログラムファイルの指定" + Code.UNIX_ENTRY);
        body.append("v_bat_id=\"" + shellId + "\"" + Code.UNIX_ENTRY);
        body.append("v_bat_name=\"" + shellName + "\"" + Code.UNIX_ENTRY);
        body.append("v_plpgsql_name=\"" + shellId + "\"" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#バッチログファイル定義" + Code.UNIX_ENTRY);
        body.append("v_log_file=${ENV_BATCH_LOG}" + Code.UNIX_ENTRY);
        body.append("#リターンコードの初期化" + Code.UNIX_ENTRY);
        body.append("v_rc=0" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("INFO_MSG=`GetMsg M0002`" + Code.UNIX_ENTRY);
        body.append("#処理異常終了" + Code.UNIX_ENTRY);
        body.append("ERROR_MSG=`GetMsg M0003`" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#開始ログの出力" + Code.UNIX_ENTRY);
        body.append("MSG001=`GetMsg M0001`" + Code.UNIX_ENTRY);
        body.append("PutLog \"Info\" \"${v_bat_id}\" \"${v_bat_name}\" \"${MSG001}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#プロシージャ開始ログの出力" + Code.UNIX_ENTRY);
        body.append("MSG015=`GetMsg M0015 \"${v_bat_name}\"`" + Code.UNIX_ENTRY);
        body.append("PutLog \"Info\" \"${v_bat_id}\" \"${v_bat_name}\" \"${MSG015}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#プロシージャ呼び出し" + Code.UNIX_ENTRY);
        body.append("v_value=`psql -h\"${PGHOST}\" -d\"${PGDATABASE}\" << EOF" + Code.UNIX_ENTRY);
        body.append("select NJ_B_DB0040('${BCD}')" + Code.UNIX_ENTRY);
        body.append("EOF`" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("v_rc=`echo $v_value | awk -F' ' '{print $3}'`" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("     #処理結果ログ" + Code.UNIX_ENTRY);
        body.append("    if [[ $v_rc -lt 90 ]]; then" + Code.UNIX_ENTRY);
        body.append("         #プロシージャ処理正常終了ログの出力" + Code.UNIX_ENTRY);
        body.append("       MSG016=`GetMsg M0016 ${v_plpgsql_name}`" + Code.UNIX_ENTRY);
        body.append("       PutLog \"Info\" \"${v_bat_id}\" \"${v_bat_name}\" \"${MSG016}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("    else " + Code.UNIX_ENTRY);
        body.append("         #プロシージャ処理異常終了ログの出力" + Code.UNIX_ENTRY);
        body.append("       MSG017=`GetMsg M0017 ${v_plpgsql_name}`" + Code.UNIX_ENTRY);
        body.append("       PutLog \"Error\" \"${v_bat_id}\" \"${v_bat_name}\" \"${MSG017}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("    fi" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#処理結果ログ" + Code.UNIX_ENTRY);
        body.append("if [[ $v_rc -lt 90 ]]; then" + Code.UNIX_ENTRY);
        body.append("     #処理正常終了" + Code.UNIX_ENTRY);
        body.append("    END_MSG=`GetMsg M0002`" + Code.UNIX_ENTRY);
        body.append("    END_LOG_LEVEL=\"Info\"" + Code.UNIX_ENTRY);
        body.append("else " + Code.UNIX_ENTRY);
        body.append("     #処理異常終了" + Code.UNIX_ENTRY);
        body.append("    END_MSG=`GetMsg M0003`" + Code.UNIX_ENTRY);
        body.append("    END_LOG_LEVEL=\"Error\"" + Code.UNIX_ENTRY);
        body.append("fi" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#返却値ログ" + Code.UNIX_ENTRY);
        body.append("RETURN_MSG=`GetMsg M0021 \"${v_rc}\"`" + Code.UNIX_ENTRY);
        body.append("PutLog ${END_LOG_LEVEL} \"${v_bat_id}\" \"${v_bat_name}\" \"${RETURN_MSG}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("#処理終了ログ" + Code.UNIX_ENTRY);
        body.append("PutLog ${END_LOG_LEVEL} \"${v_bat_id}\" \"${v_bat_name}\" \"${END_MSG}\" \"${v_log_file}\"" + Code.UNIX_ENTRY);
        body.append("" + Code.UNIX_ENTRY);
        body.append("#終了コードの返却" + Code.UNIX_ENTRY);
        body.append("exit $v_rc");
        return body.toString();
    }

    private String createProcCmd() {
        StringBuilder cmd = new StringBuilder();
        cmd.append("---------------------------------------------------------------" + Code.WINDOWS_ENTRY);
        cmd.append("-- "+shellName + Code.WINDOWS_ENTRY);
        cmd.append("-- businesscd    VARCHAR" + Code.WINDOWS_ENTRY);
        cmd.append("-- 戻り値          integer（[90]システムエラー　[91]データ無エラー）" + Code.WINDOWS_ENTRY);
        cmd.append("----------------------------------------------------------------" + Code.WINDOWS_ENTRY);
        cmd.append(Code.WINDOWS_ENTRY);
        return cmd.toString();
    }

    private String createProcBody() {
        StringBuilder body = new StringBuilder();
        body.append("CREATE OR REPLACE FUNCTION NJ_B_DB0040(businessCd VARCHAR) " + Code.WINDOWS_ENTRY);
        body.append("RETURNS INT AS " + Code.WINDOWS_ENTRY);
        body.append("'" + Code.WINDOWS_ENTRY);
        body.append("DECLARE " + Code.WINDOWS_ENTRY);
        body.append("    pbusiness_cd             VARCHAR;         -- 業態コード" + Code.WINDOWS_ENTRY);
        body.append("    pexecute_datetime        VARCHAR;         -- 起動日時" + Code.WINDOWS_ENTRY);
        body.append("    pid                      VARCHAR;         -- 実行識別名" + Code.WINDOWS_ENTRY);
        body.append("    pend_datetime            VARCHAR;         -- 終了日時" + Code.WINDOWS_ENTRY);
        body.append("    pdisposition_id          VARCHAR;         -- 処理ID" + Code.WINDOWS_ENTRY);
        body.append("    pstatus                  VARCHAR;         -- 実行ステータス" + Code.WINDOWS_ENTRY);
        body.append("    pmessage_1               VARCHAR;         -- エラーメッセージ（１）" + Code.WINDOWS_ENTRY);
        body.append("    pmessage_2               VARCHAR;         -- エラーメッセージ（２）" + Code.WINDOWS_ENTRY);
        body.append("    precord_reg_user_cd      VARCHAR;         -- 登録者コード" + Code.WINDOWS_ENTRY);
        body.append("    precord_reg_date         VARCHAR;         -- 登録日時" + Code.WINDOWS_ENTRY);
        body.append("    precord_upd_user_cd      VARCHAR;         -- 最終更新者コード" + Code.WINDOWS_ENTRY);
        body.append("    precord_upd_date         VARCHAR;         -- 最終更新日時" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("BEGIN" + Code.WINDOWS_ENTRY);
        body.append("    pbusiness_cd := businessCd;" + Code.WINDOWS_ENTRY);
        body.append("    pexecute_datetime := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');" + Code.WINDOWS_ENTRY);
        body.append("    pid := ''"+shellId+"'';" + Code.WINDOWS_ENTRY);
        body.append("    pdisposition_id := ''"+shellId+"'';" + Code.WINDOWS_ENTRY);
        body.append("    pstatus := ''1'';" + Code.WINDOWS_ENTRY);
        body.append("    pmessage_1 := ''開始しました'';" + Code.WINDOWS_ENTRY);
        body.append("    precord_reg_date := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');" + Code.WINDOWS_ENTRY);
        body.append("    precord_reg_user_cd:= ''SYSTEM'';" + Code.WINDOWS_ENTRY);
        body.append("    precord_upd_date := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');" + Code.WINDOWS_ENTRY);
        body.append("    precord_upd_user_cd:= ''SYSTEM'';" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("    -- バッチログ作成" + Code.WINDOWS_ENTRY);
        body.append("    CALL family_batchlog_insert(pbusiness_cd, pexecute_datetime, pid, pend_datetime, pdisposition_id, pstatus, pmessage_1, pmessage_2, precord_reg_user_cd, precord_reg_date, precord_upd_user_cd, precord_upd_date);" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("    BEGIN" + Code.WINDOWS_ENTRY);
        body.append("        -- 業務書くエリア" + Code.WINDOWS_ENTRY);
        body.append("        " + Code.WINDOWS_ENTRY);
        body.append("        " + Code.WINDOWS_ENTRY);
        body.append("    EXCEPTION" + Code.WINDOWS_ENTRY);
        body.append("        WHEN OTHERS THEN" + Code.WINDOWS_ENTRY);
        body.append("        RAISE EXCEPTION ''(%)'',SQLERRM;" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("        pstatus := ''2'';                                                     -- 実行ステータス(2:異常終了)" + Code.WINDOWS_ENTRY);
        body.append("        pmessage_1 := ''異常終了'';                                            -- エラーメッセージ（１）" + Code.WINDOWS_ENTRY);
        body.append("        pmessage_2 := ''店舗発注累積データ作成|'' || SQLERRM;                     -- エラーメッセージ（２）" + Code.WINDOWS_ENTRY);
        body.append("        pexecute_datetime := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');       --起動日時" + Code.WINDOWS_ENTRY);
        body.append("        pend_datetime := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');           --終了日時" + Code.WINDOWS_ENTRY);
        body.append("        precord_upd_date := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');        --最終更新日時" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("        -- バッチログ更新" + Code.WINDOWS_ENTRY);
        body.append("        CALL family_batchlog_update(pbusiness_cd, pexecute_datetime, pid, pend_datetime, pstatus, pmessage_1, pmessage_2, precord_upd_user_cd, precord_upd_date);" + Code.WINDOWS_ENTRY);
        body.append("        RETURN 90;" + Code.WINDOWS_ENTRY);
        body.append("    END;" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("    pstatus := ''3'';                                                     -- 実行ステータス(3:正常終了)" + Code.WINDOWS_ENTRY);
        body.append("    pmessage_1 := ''正常終了'';                                             -- エラーメッセージ（１）" + Code.WINDOWS_ENTRY);
        body.append("    pexecute_datetime := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');       --起動日時" + Code.WINDOWS_ENTRY);
        body.append("    pend_datetime := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');           --終了日時" + Code.WINDOWS_ENTRY);
        body.append("    precord_upd_date := to_char(now(), ''yyyy/mm/dd|hh24:mi:ss'');        --最終更新日時" + Code.WINDOWS_ENTRY);
        body.append("" + Code.WINDOWS_ENTRY);
        body.append("    -- バッチログ更新" + Code.WINDOWS_ENTRY);
        body.append("    CALL family_batchlog_update(pbusiness_cd, pexecute_datetime, pid, pend_datetime, pstatus, pmessage_1, pmessage_2, precord_upd_user_cd, precord_upd_date);" + Code.WINDOWS_ENTRY);
        body.append("    RETURN 0;" + Code.WINDOWS_ENTRY);
        body.append("END;" + Code.WINDOWS_ENTRY);
        body.append("'" + Code.WINDOWS_ENTRY);
        body.append(" LANGUAGE plpgsql;" + Code.WINDOWS_ENTRY);
        return body.toString();
    }
}