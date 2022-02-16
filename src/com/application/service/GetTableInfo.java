package com.application.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.application.common.Context;
import com.application.dto.Table;
import com.application.dto.Table.Column;
import com.application.util.FileUtils;
import com.application.util.TextUtil;

/**
 * 通过DDL生成Table信息
 *
 * @author dylsw
 *
 */
public class GetTableInfo {

    public Table getTableInfoFromFile(String fileName) {
        String path = FileUtils.getPath() + "\\" + fileName;
        List<String> ddlText = FileUtils.getFileText(new File(path));

        return getTable(ddlText);
    }

    private Table getTable(List<String> ddlText) {
        // 表对象
        Table table = new Table();
        // 表中字段对象
        List<Column> tblColList = new ArrayList<Table.Column>();
        // 唯一值字段
        List<Column> uniqueKey = new ArrayList<Table.Column>();

        // 设置
        for (int i = 0; i < ddlText.size(); i++) {
            // 转大写
            String line = ddlText.get(i).toUpperCase();
            // 基本信息
            if (i == 0) {
                String baseInfo = line.split(Context.SPACE)[2];
                System.out.println(baseInfo);
                if (baseInfo.indexOf(Context.POINT) > 0) {
                    // DB名
                    table.setDatabaseName(baseInfo.split("\\.")[0]);
                    // 表名
                    table.setName(baseInfo.split("\\.")[1]);
                }
                continue;
            }
            // 字段信息
            if (line.indexOf("--") > 0) {
                // 字段通用属性
                Column col = table.getColumnInstance();
                // 字段伦理名
                String logicName = line.split("--")[1].trim();
                col.setLogicName(logicName);
                // 字段
                String[] columnInfo = line.split(Context.SPACE);
                // 物理名
                String baseNm = columnInfo[0].replace(Context.COMMA, "").trim();
                col.setColumnName(baseNm);
                col.setSourceName(TextUtil.transNmDbToJava(baseNm, false));
                // 类型
                String baseType = columnInfo[1].trim();
                // 长度
                int lengthIndex = baseType.indexOf("(");
                if (lengthIndex > 0) {
                    String colType = baseType.substring(0, lengthIndex);
                    col.setColumnType(colType);
                    String length = baseType.substring(lengthIndex + 1, baseType.length() - 1);
                    col.setLength(Integer.parseInt(length));
                    col.setJavaType(javaTypeMap(colType, Integer.parseInt(length)));
                } else {
                    col.setColumnType(baseType);
                    col.setJavaType(javaTypeMap(baseType, 0));
                }

                tblColList.add(col);
                // 特殊字段-主键
                if (logicName.indexOf("ID") >= 0 && logicName.indexOf("PRIMARYKEY") > 0) {
                    table.setPkey(col);
                } else if (logicName.indexOf("UNIQUE") >= 0) {
                    // 逻辑主键
                    uniqueKey.add(col);
                }
            }
        }

        table.setColumnList(tblColList);
        table.setUniqueKey(uniqueKey);

        return table;
    }

    private String javaTypeMap(String dbType, int length) {
        String type = "String";
        if ("NUMBER".equals(dbType)) {
            type = "Integer";
        } else if ("TIMESTAMP".equals(dbType)) {
            type = "Date";
        }
        return type;
    }
}
