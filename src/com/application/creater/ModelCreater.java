package com.application.creater;

import com.application.common.Context;
import com.application.dto.Table;
import com.application.util.TextUtil;

public class ModelCreater extends BaseCreater {

    public String createModel(Table tableObj, String projectName, String pkgNm, boolean fileFlag,
            boolean hibernateFlag) {

        StringBuilder modelStr = new StringBuilder();
        pkgNm = pkgNm + ".model";
        modelStr.append("package " + pkgNm + ";" + Context.CRLF2);
        modelStr.append("import java.util.Date;" + Context.CRLF2);
        // hibernate
        if (hibernateFlag) {
            modelStr.append("import javax.persistence.*;" + Context.CRLF2);
            modelStr.append("@Entity" + Context.CRLF);
            modelStr.append("@Table(name=\"" + tableObj.getName() + "\")" + Context.CRLF);
        }

        String fileNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        modelStr.append("public class " + fileNm + " extends ExpandCondition {" + Context.CRLF);

        // 参数部分
        StringBuilder param = new StringBuilder();
        // get和set部分
        StringBuilder setAndGet = new StringBuilder();

        for (Table.Column field : tableObj.getColumnList()) {
            // 伦理名
            String logicNm = field.getLogicName();
            // java命名
            String javaNm = field.getSourceName();
            // 首字母大写
            String metNm = TextUtil.transNmDbToJava(field.getColumnName(), true);
            // 类型
            String type = field.getJavaType();
            // 说明doc
            param.append(Context.SPACE4 + "/**" + Context.CRLF);
            param.append(Context.SPACE4 + " * " + logicNm + Context.CRLF);
            param.append(Context.SPACE4 + " */" + Context.CRLF);

            // hibernate
            if (hibernateFlag) {
                if ("ID".equals(field.getColumnName())) {
                    param.append(Context.SPACE4 + "@Id" + Context.CRLF);
                }
                param.append(Context.SPACE4 + "@Column(name=\"" + field.getColumnName() + "\")" + Context.CRLF);
            }

            // 输出参数
            param.append(Context.SPACE4 + "private " + field.getJavaType() + " " + javaNm + ";" + Context.CRLF
                    + Context.CRLF);

            // 输出set和get
            setAndGet.append(Context.SPACE4 + "/**" + Context.CRLF);
            setAndGet.append(Context.SPACE4 + " * 设定" + logicNm + Context.CRLF);
            setAndGet.append(Context.SPACE4 + " */" + Context.CRLF);
            setAndGet.append(
                    Context.SPACE4 + "public void set" + metNm + "(" + type + " " + javaNm + "){" + Context.CRLF);
            if ("String".equals(type)) {
                setAndGet.append(Context.SPACE4 + "    this." + javaNm + " = null == " + javaNm + " ? \"\" : " + javaNm
                        + ";" + Context.CRLF);
            } else {
                setAndGet.append(Context.SPACE4 + "    this." + javaNm + " = " + javaNm + ";" + Context.CRLF);
            }
            setAndGet.append(Context.SPACE4 + "}" + Context.CRLF2);
            setAndGet.append(Context.SPACE4 + "/**" + Context.CRLF);
            setAndGet.append(Context.SPACE4 + " * 取得" + logicNm + Context.CRLF);
            setAndGet.append(Context.SPACE4 + " */" + Context.CRLF);
            setAndGet.append(Context.SPACE4 + "public " + type + " get" + metNm + "(){" + Context.CRLF);
            setAndGet.append(Context.SPACE4 + "    return " + javaNm + ";" + Context.CRLF);
            setAndGet.append(Context.SPACE4 + "}" + Context.CRLF2);
        }

        modelStr.append(param.toString());
        modelStr.append(setAndGet.toString());
        modelStr.append(Context.CRLF + "}");

        if (fileFlag) {
            super.writeFile(projectName, pkgNm, fileNm + ".java", modelStr.toString());
        }
        return modelStr.toString();

    }

}
