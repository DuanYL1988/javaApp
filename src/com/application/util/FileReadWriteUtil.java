package com.application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Code;

public class FileReadWriteUtil {

    public String getLocalPath() {
        File file = new File("");
        return file.getAbsolutePath() + "\\" + "data.pswd";
    }

    public void initFile(String filePath) {
        File localFile = new File(filePath);
        try {
            if (!localFile.exists()) {
                System.out.println("文件不存在，新规做成");
                localFile.createNewFile();
                OutputStreamWriter pw = null;
                pw = new OutputStreamWriter(new FileOutputStream(filePath),
                        Code.CHARSET_CODE_UTF8);
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 只匹配登陆先
     */
    public String getData(String websit, String path, boolean bank_kbn) {
        SecurityUtil security = new SecurityUtil();
        List<String> resultList = new ArrayList<String>();
        StringBuffer resultText = new StringBuffer();
        // String debug = "文件流读取的内容";
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line = null;
            while ((line = reader.readLine()) != null) {
                // debug = TextUtil.debugLog(debug, null, line);
                if (line.indexOf(websit) >= 0) {
                    String[] spilt = line.split(Code.SPILT);
                    resultList.add(spilt[1]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultList.size() == 0) {
            resultText.append(Code.MSG_SELECT_MARRING);
            // TextUtil.debugLog(resultText, "file text \n", debug);
        } else {
            resultText.append(Code.MSG_SELECT_SUCCESS);
            for (String result : resultList) {
                result = result.replaceAll("\\\\", "");
                String result_account = result.substring(0,
                        result.length() - 25);
                String result_password = result.substring(result.length() - 24,
                        result.length());
                // 模糊查询时不解密账号
                if (bank_kbn) {
                    resultText.append(
                            "\n账号(卡号):" + security.deCrypt(result_account));
                } else {
                    resultText.append("\n账号(卡号):" + result_account);
                }
                resultText.append(
                        "\n密码:" + security.deCrypt(result_password) + "\n");
            }
        }
        return resultText.toString();
    }

}
