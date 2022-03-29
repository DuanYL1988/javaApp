package com.application.creater;

import java.util.List;

import com.application.common.Context;
import com.application.dto.Table;
import com.application.util.TextUtil;

public class DaoCreater extends BaseCreater {

    private String resultMapNm = "";

    public DaoCreater(Table tableObj) {
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        resultMapNm = modelNm + "ResultMap";
    }

    /**
     * 创建Mybatis接口
     *
     * @param  tableObj      表对象
     * @param  projectName   项目名
     * @param  pkgNm         包名
     * @param  fileFlag      生成文件flag
     * @param  hibernateFlag 使用Hibernate
     * @return
     */
    public String createMybatis(Table tableObj, String projectName, String pkgNm, boolean fileFlag,
            boolean hibernateFlag) {

        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        // 实体类变量名
        String modelVarNm = modelNm.toLowerCase();
        String fileNm = modelNm + "Repository";

        StringBuilder daoStr = new StringBuilder();
        // Package
        String thisPkg = pkgNm + ".dao";
        daoStr.append("package " + pkgNm + ".dao;" + Context.CRLF2);

        // 关联对象import
        daoStr.append("import java.util.List;" + Context.CRLF);
        daoStr.append("import org.apache.ibatis.annotations.Param;" + Context.CRLF);
        daoStr.append("import org.springframework.stereotype.Repository;" + Context.CRLF2);

        daoStr.append("import " + pkgNm + ".model." + modelNm + ";" + Context.CRLF2);

        daoStr.append("@Repository" + Context.CRLF);
        daoStr.append("public interface " + fileNm + " {" + Context.CRLF2);

        daoStr.append("    " + modelNm + " selectOneById(@Param(\"id\")String id);" + Context.CRLF2);
        // 使用hibernate时和ID相关的Mbatis不执行
        if (!hibernateFlag) {
            daoStr.append("    void insert(" + modelNm + " " + modelVarNm + ");" + Context.CRLF2);
            daoStr.append("    void update(" + modelNm + " " + modelVarNm + ");" + Context.CRLF2);
        }

        daoStr.append("    " + modelNm + " selectOneByUniqueKey(" + editUnionKey(tableObj) + ");" + Context.CRLF2);
        daoStr.append("    List<" + modelNm + "> selectByDto(" + modelNm + " " + modelVarNm + ");" + Context.CRLF2);
        daoStr.append("    List<" + modelNm + "> customQuary(" + modelNm + " " + modelVarNm + ");" + Context.CRLF2);

        daoStr.append("}" + Context.CRLF2);

        if (fileFlag) {
            super.writeFile(projectName, thisPkg, fileNm + ".java", daoStr.toString());
        }

        return daoStr.toString();
    }

    /**
     * @param  tableObj      表对象
     * @param  projectName   项目名
     * @param  pkgNm         包名
     * @param  fileFlag      生成文件flag
     * @param  hibernateFlag 使用Hibernate
     * @return
     */
    public String createMappingXml(Table tableObj, String projectName, String pkgNm, boolean fileFlag,
            boolean hibernateFlag) {
        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        String fileNm = modelNm + "Repository";

        // myBatis的xml
        StringBuilder xmlStr = new StringBuilder();
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + Context.CRLF);
        xmlStr.append(
                "<!DOCTYPE mapper PUBLIC \"-//MYBATIS.org//DTD Mapper 3.0//EN\" \"http://MYBATIS.org/dtd/MYBATIS-3-mapper.dtd\" >"
                        + Context.CRLF);
        xmlStr.append("<mapper namespace=\"" + pkgNm + ".dao." + modelNm + "Repository\" >" + Context.CRLF);

        String paramStr = pkgNm + ".model." + modelNm;
        // resultMap
        xmlStr.append(createResultMap(tableObj, pkgNm));
        // select字段
        xmlStr.append(createSelectCol(tableObj, "select"));
        // insert字段
        xmlStr.append(createSelectCol(tableObj, "insert"));
        // selectOne
        xmlStr.append(createSelectOneById(tableObj));
        if (!hibernateFlag) {
            // update
            xmlStr.append(createUpdate(tableObj, paramStr));
            // insert
            xmlStr.append(createInsert(tableObj, paramStr));
        }

        // selectUniqueOne
        xmlStr.append(createSelectOneByUniqueKey(tableObj));
        // selectByDto
        xmlStr.append(createSelectByDto(tableObj, paramStr));

        xmlStr.append(createCustomQuery(tableObj, tableObj.getName(), paramStr));

        // 结束标签
        xmlStr.append("</mapper>" + Context.CRLF);

        if (fileFlag) {
            super.writeFile(projectName, "", fileNm + ".xml", xmlStr.toString());
        }
        return xmlStr.toString();
    }

    /**
     * @param  tableObj
     * @param  projectName
     * @param  pkgNm
     * @param  fileFlag
     * @return
     */
    public String createHibernate(Table tableObj, String projectName, String pkgNm, boolean fileFlag) {
        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        String fileNm = modelNm + "Dao";

        StringBuilder repositoryStr = new StringBuilder();
        // Package
        String thisPkg = pkgNm + ".dao";
        repositoryStr.append("package " + pkgNm + ".dao;" + Context.CRLF2);
        repositoryStr.append("import " + pkgNm + ".model." + modelNm + ";" + Context.CRLF2);
        repositoryStr.append("import org.springframework.data.repository.CrudRepository;" + Context.CRLF);
        repositoryStr.append("import org.springframework.stereotype.Repository;" + Context.CRLF);

        repositoryStr.append("@Repository" + Context.CRLF);
        repositoryStr.append(
                "public interface " + fileNm + " extends CrudRepository<" + modelNm + ",String> {" + Context.CRLF2);

        repositoryStr.append("}" + Context.CRLF2);

        if (fileFlag) {
            super.writeFile(projectName, thisPkg, fileNm + ".java", repositoryStr.toString());
        }
        return repositoryStr.toString();
    }

    /**
     * Mybatis中的<resultMap>
     *
     * @param  table
     * @param  pkgNm
     * @return
     */
    private String createResultMap(Table table, String pkgNm) {
        StringBuilder resultMap = new StringBuilder();
        //
        String objNm = TextUtil.transNmDbToJava(table.getName(), true);

        resultMap.append("  <!-- ResultMap -->" + Context.CRLF);
        resultMap.append(
                "  <resultMap id=\"" + resultMapNm + "\" type=\"" + pkgNm + ".model." + objNm + "\" >" + Context.CRLF);
        for (Table.Column col : table.getColumnList()) {
            resultMap.append(Context.SPACE4);
            resultMap.append("<result column=\"" + col.getColumnName() + "\" ");
            resultMap.append("property=\"" + col.getSourceName() + "\" ");
            resultMap.append("jdbcType=\"" + col.getColumnType() + "\" />" + Context.CRLF);
        }
        resultMap.append("  </resultMap>" + Context.CRLF2);

        return resultMap.toString();
    }

    /**
     * select字段
     */
    private String createSelectCol(Table table, String colDiv) {
        StringBuilder selectColumns = new StringBuilder();
        if ("select".equals(colDiv)) {
            selectColumns.append("  <sql id=\"select_column_list\">" + Context.CRLF);
        } else {
            selectColumns.append("  <sql id=\"insert_column_list\">" + Context.CRLF);
        }
        boolean firstLine = true;
        for (Table.Column field : table.getColumnList()) {
            String comma = ",";
            if (firstLine) {
                comma = "";
                firstLine = false;
            }
            if ("select".equals(colDiv)) {
                selectColumns
                        .append(Context.SPACE4 + comma + table.getName() + "." + field.getColumnName() + Context.CRLF);
            } else {
                selectColumns.append(Context.SPACE4 + comma + field.getColumnName() + Context.CRLF);
            }
            firstLine = false;
        }
        selectColumns.append("  </sql>" + Context.CRLF2);
        return selectColumns.toString();
    }

    /**
     * 通过ID主键查找唯一1条
     */
    private String createSelectOneById(Table table) {
        StringBuilder selectOne = new StringBuilder();
        selectOne.append(
                Context.SPACE2 + "<select id=\"selectOneById\" resultMap=\"" + resultMapNm + "\">" + Context.CRLF);
        selectOne.append(Context.SPACE4 + "SELECT" + Context.CRLF);
        selectOne.append(Context.SPACE6 + "<include refid=\"select_column_list\"></include>" + Context.CRLF);
        selectOne.append(Context.SPACE4 + "FROM " + Context.CRLF);
        selectOne.append(Context.SPACE6 + table.getName() + Context.CRLF);
        selectOne.append(Context.SPACE4 + "WHERE" + Context.CRLF);

        // 主键
        Table.Column field = table.getPkey();
        selectOne.append("      " + field.getColumnName() + " = " + "#{" + field.getSourceName() + "}" + Context.CRLF);
        selectOne.append(Context.SPACE2 + "</select>" + Context.CRLF + Context.CRLF);

        return selectOne.toString();
    }

    /**
     * 业务逻辑上唯一1条
     */
    private String createSelectOneByUniqueKey(Table table) {
        StringBuilder selectOne = new StringBuilder();
        selectOne.append(Context.SPACE2 + "<select id=\"selectOneByUniqueKey\" resultMap=\"" + resultMapNm + "\">"
                + Context.CRLF);
        selectOne.append(Context.SPACE4 + "SELECT" + Context.CRLF);
        selectOne.append(Context.SPACE6 + "<include refid=\"select_column_list\"></include>" + Context.CRLF);
        selectOne.append(Context.SPACE4 + "FROM " + Context.CRLF);
        selectOne.append(Context.SPACE6 + table.getName() + Context.CRLF);
        selectOne.append(Context.SPACE4 + "<where>" + Context.CRLF);
        for (Table.Column field : table.getUniqueKey()) {
            appendCheckCols(selectOne, field, "AND");
        }
        selectOne.append(Context.SPACE4 + "</where>" + Context.CRLF);
        selectOne.append(Context.SPACE2 + "</select>" + Context.CRLF + Context.CRLF);
        return selectOne.toString();
    }

    /**
     * 复杂查询
     */
    private String createSelectByDto(Table table, String parameterType) {
        StringBuilder selectByDto = new StringBuilder();
        selectByDto.append(Context.SPACE2 + "<select id=\"selectByDto\" parameterType=\"" + parameterType
                + "\" resultMap=\"" + resultMapNm + "\">" + Context.CRLF);
        selectByDto.append(Context.SPACE4 + "SELECT" + Context.CRLF);
        selectByDto.append(Context.SPACE6 + "<include refid=\"select_column_list\"></include>" + Context.CRLF);
        selectByDto.append(Context.SPACE4 + "FROM " + Context.CRLF);
        selectByDto.append(Context.SPACE6 + table.getName() + Context.CRLF);
        selectByDto.append(Context.SPACE4 + "<where>" + Context.CRLF);
        selectByDto.append(createColCheck(table.getColumnList(), "select"));
        selectByDto.append(Context.SPACE6 + "<if test=\"condition!=null and condition!=''\">" + Context.CRLF);
        selectByDto.append(Context.SPACE4 + Context.SPACE4 + "${condition}" + Context.CRLF);
        selectByDto.append(Context.SPACE6 + "</if>" + Context.CRLF);
        selectByDto.append(Context.SPACE4 + "</where>" + Context.CRLF);
        // TODO 需要自定义类
        // selectByDto.append(Context.SPACE4 + "<if test=\"orderBy!=null\">" + Context.CRLF);
        // selectByDto.append(Context.SPACE4 + " ORDER BY ${orderBy}" + Context.CRLF);
        // selectByDto.append(Context.SPACE4 + "</if>" + Context.CRLF);
        selectByDto.append(Context.SPACE2 + "</select>" + Context.CRLF + Context.CRLF);
        return selectByDto.toString();
    }

    /**
     * 更新
     */
    private String createUpdate(Table table, String parameterType) {
        StringBuilder update = new StringBuilder();
        update.append(
                Context.SPACE2 + "<update id=\"update\" parameterType=\"" + parameterType + "\" >" + Context.CRLF);
        update.append(Context.SPACE4 + "UPDATE" + Context.CRLF);
        update.append(Context.SPACE6 + table.getName() + Context.CRLF);
        update.append(Context.SPACE4 + "<set>" + Context.CRLF);
        update.append(createColCheck(table.getColumnList(), "update"));
        update.append(Context.SPACE4 + "</set>" + Context.CRLF);
        update.append(Context.SPACE4 + "WHERE" + Context.CRLF);
        //
        Table.Column pkCol = table.getPkey();
        update.append(
                Context.SPACE6 + pkCol.getColumnName() + " = " + "#{" + pkCol.getSourceName() + "}" + Context.CRLF);
        // TODO 需要自定义类
        // update.append(Context.SPACE4 + "<if test=\"condition!=null\">" + Context.CRLF);
        // update.append(Context.SPACE4 + " OR ${condition}" + Context.CRLF);
        // update.append(Context.SPACE4 + "</if>" + Context.CRLF);
        update.append(Context.SPACE2 + "</update>" + Context.CRLF + Context.CRLF);
        return update.toString();
    }

    /**
     * 追加
     */
    private String createInsert(Table table, String parameterType) {
        String tableNm = table.getName();
        StringBuilder insert = new StringBuilder();
        insert.append("  <insert id=\"insert\" parameterType=\"" + parameterType + "\" >" + Context.CRLF);
        // TODO Oracle中用sequence
        // insert.append(Context.SPACE4 + Context.SPACE4
        // + "<selectKey resultType=\"java.lang.Integer\" order=\"BEFORE\" keyProperty=\"id\">" + Context.CRLF);
        // insert.append(Context.SPACE4 + Context.SPACE6 + "SELECT SEQ_" + tableNm + ".NEXTVAL FROM DUAL" + Context.CRLF);
        // insert.append(Context.SPACE4 + Context.SPACE4 + "</selectKey>" + Context.CRLF);
        insert.append(Context.SPACE4 + "INSERT INTO " + tableNm + "(" + Context.CRLF);
        insert.append(Context.SPACE4 + "<!-- <trim prefix='(' suffix=')' suffixOverrides=','> -->" + Context.CRLF);
        insert.append(Context.SPACE4 + "<include refid=\"insert_column_list\"></include>" + Context.CRLF);
        // insert.append(Context.SPACE4+") VALUES ("+CRLF);
        insert.append(Context.SPACE4 + ") SELECT " + Context.CRLF);
        boolean firstFlg = true;
        String uniqueCondition = Context.SPACE4 + " WHERE 1=1 ";
        for (Table.Column field : table.getColumnList()) {
            String comma = ",";
            if (firstFlg) {
                comma = "";
                firstFlg = false;
            }
            insert.append(Context.SPACE6 + comma + " #{" + field.getSourceName() + "}" + Context.CRLF);
        }
        for (int i = 0; i < table.getUniqueKey().size(); i++) {
            Table.Column uniqueCol = table.getUniqueKey().get(i);
            String javaNm = uniqueCol.getSourceName();
            String dbNm = uniqueCol.getColumnName();
            uniqueCondition += Context.CRLF + Context.SPACE4 + Context.SPACE4 + " AND " + dbNm + " = " + "#{" + javaNm
                    + "} ";
        }
        // insert.append(Context.SPACE4+")"+CRLF);
        insert.append(Context.SPACE4 + " FROM DUAL " + Context.CRLF);
        insert.append(Context.SPACE4 + " WHERE 1=1 " + Context.CRLF);
        insert.append(Context.SPACE4 + " AND NOT EXISTS " + Context.CRLF);
        insert.append(Context.SPACE4 + " (SELECT 1 FROM " + tableNm + Context.CRLF);
        insert.append(Context.SPACE4 + uniqueCondition + ")" + Context.CRLF);
        insert.append(Context.SPACE4 + " AND ROWNUM = 1" + Context.CRLF);
        insert.append(Context.SPACE2 + "</insert>" + Context.CRLF + Context.CRLF);
        return insert.toString();
    }

    /**
     * 自定义SQL查询
     *
     * @param  table
     * @param  pkgNm
     * @return
     */
    @SuppressWarnings("unused")
    private String createCustomQuery(Table table, String tableNm, String paramStr) {
        StringBuilder custom = new StringBuilder();

        custom.append("  <select id=\"customQuary\" parameterType=\"" + paramStr + "\" resultType=\"" + paramStr + "\">"
                + Context.CRLF);
        custom.append(Context.SPACE4 + "SELECT" + Context.CRLF);
        custom.append("       ${selectQuary}" + Context.CRLF);
        custom.append("    FROM" + Context.CRLF);
        custom.append(Context.SPACE6 + tableNm + Context.CRLF);
        custom.append("    <if test=\"joinPart!=null\">" + Context.CRLF);
        custom.append("        ${joinPart}" + Context.CRLF);
        custom.append("    </if>" + Context.CRLF);
        custom.append("    <where>" + Context.CRLF);
        custom.append("        1 = 1" + Context.CRLF);
        custom.append("        <if test=\"condition!=null\">" + Context.CRLF);
        custom.append("         AND ${condition}" + Context.CRLF);
        custom.append("        </if>" + Context.CRLF);
        custom.append("    </where>" + Context.CRLF);
        custom.append("    <if test=\"groupBy!=null\">" + Context.CRLF);
        custom.append("      GROUP BY ${groupBy}" + Context.CRLF);
        custom.append("    </if>" + Context.CRLF);
        custom.append("    <if test=\"having!=null\">" + Context.CRLF);
        custom.append("      HAVING ${having}" + Context.CRLF);
        custom.append("    </if>" + Context.CRLF);
        custom.append("    <if test=\"orderBy!=null\">" + Context.CRLF);
        custom.append("      ORDER BY ${orderBy}" + Context.CRLF);
        custom.append("    </if>" + Context.CRLF);
        custom.append("  </select>" + Context.CRLF + Context.CRLF);

        return custom.toString();
    }

    /**
     * MYBATIS使用tag自动去除AND,逗号时使用
     */
    private void appendCheckCols(StringBuilder condition, Table.Column field, String mark) {
        String javaNm = field.getSourceName();
        condition.append(Context.SPACE6 + "<if test=\"" + javaNm + "!=null and " + javaNm + "!=''\">" + Context.CRLF);
        if ("AND".equals(mark)) {
            condition.append(Context.SPACE4 + Context.SPACE4 + mark + " " + field.getColumnName() + " = " + "#{"
                    + javaNm + "}" + Context.CRLF);
        } else if (",".equals(mark)) {
            condition.append(Context.SPACE4 + Context.SPACE4 + field.getColumnName() + " = " + "#{" + javaNm + "}"
                    + mark + Context.CRLF);
        }
        condition.append(Context.SPACE6 + "</if>" + Context.CRLF);
    }

    /**
     * MYBATIS的字段空判断 创建select文时KBN传入"select" select文因为在<where>标签中所以不用加[,]
     *
     * @param  构筑函数中取得的表中所有的字段集合 fields
     * @param  select和update的区分  KBN
     * @return
     */
    private String createColCheck(List<Table.Column> fields, String kbn) {
        StringBuilder condition = new StringBuilder();
        boolean updateFlag = "update".equals(kbn);
        String comma = "AND";
        if (updateFlag) {
            comma = ",";
        }
        for (Table.Column field : fields) {
            // update时不更新主键
            if (Context.TBL_PK.equals(field.getDiv()) && updateFlag) {
                continue;
            }
            appendCheckCols(condition, field, comma);
        }
        return condition.toString();
    }

    private String editUnionKey(Table table) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Table.Column column : table.getUniqueKey()) {
            if (index > 0) {
                sb.append(",");
            }
            sb.append("@Param(\"" + column.getSourceName() + "\")");
            sb.append(column.getJavaType() + " " + column.getSourceName());
            index++;
        }
        return sb.toString();
    }

}
