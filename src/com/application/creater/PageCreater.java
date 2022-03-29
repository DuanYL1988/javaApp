package com.application.creater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.application.common.Context;
import com.application.dto.Table;
import com.application.util.TextUtil;

public class PageCreater extends BaseCreater {

    /**
     * 创建Controller类
     *
     * @param  tableObj
     * @param  pkgNm
     * @return
     */
    public String createController(Table tableObj, String projectName, String pkgNm, boolean fileFlag,
            boolean hibernateFlag) {

        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        // 实体类变量名
        String modelVarNm = modelNm.toLowerCase();

        StringBuilder ctrlStr = new StringBuilder();
        // Package
        String thisPkg = pkgNm + ".controller";
        String fileNm = modelNm + "Controller";
        ctrlStr.append("package " + thisPkg + ";" + Context.CRLF2);
        // Import
        ctrlStr.append("import java.util.*;" + Context.CRLF);

        // 关联对象import
        ctrlStr.append("import " + pkgNm + ".service." + modelNm + "Service;" + Context.CRLF);
        ctrlStr.append("import " + pkgNm + ".dao." + modelNm + "Repository;" + Context.CRLF);
        if (hibernateFlag) {
            ctrlStr.append("import " + pkgNm + ".dao." + modelNm + "Dao;" + Context.CRLF);
        }
        ctrlStr.append("import " + pkgNm + ".model." + modelNm + ";" + Context.CRLF2);

        ctrlStr.append("import org.springframework.beans.factory.annotation.Autowired;" + Context.CRLF);
        ctrlStr.append("import org.springframework.stereotype.Controller;" + Context.CRLF);
        ctrlStr.append("import org.springframework.ui.Model;" + Context.CRLF);
        ctrlStr.append("import org.springframework.web.bind.annotation.PathVariable;" + Context.CRLF);
        ctrlStr.append("import org.springframework.web.bind.annotation.RequestBody;" + Context.CRLF);
        ctrlStr.append("import org.springframework.web.bind.annotation.RequestMapping;" + Context.CRLF);
        ctrlStr.append("import org.springframework.web.bind.annotation.ResponseBody;" + Context.CRLF2);

        // Class-Start
        ctrlStr.append("@Controller" + Context.CRLF);
        ctrlStr.append(editReqMp(modelVarNm, true));
        ctrlStr.append("public class " + fileNm + " {" + Context.CRLF2);

        // 变量
        ctrlStr.append("    @Autowired" + Context.CRLF);
        ctrlStr.append("    " + modelNm + "Service " + modelVarNm + "Service;" + Context.CRLF2);
        ctrlStr.append("    @Autowired" + Context.CRLF);
        ctrlStr.append("    " + modelNm + "Dao " + modelVarNm + "Dao;" + Context.CRLF2);
        ctrlStr.append("    @Autowired" + Context.CRLF);
        ctrlStr.append("    " + modelNm + "Repository " + modelVarNm + "Repository;" + Context.CRLF2);

        // 方法
        ctrlStr.append(createMethodIndex(modelNm, modelVarNm));
        ctrlStr.append(createMethodSearch(modelNm, modelVarNm));
        ctrlStr.append(createMethodAjaxSearch(modelNm, modelVarNm));
        ctrlStr.append(createMethodDetail(modelNm, modelVarNm, hibernateFlag));
        ctrlStr.append(createMethodUpdate(modelNm, modelVarNm, hibernateFlag));

        // Class-end
        ctrlStr.append("}" + Context.CRLF);

        if (fileFlag) {
            super.writeFile(projectName, thisPkg, fileNm + ".java", ctrlStr.toString());
        }

        return ctrlStr.toString();
    }

    /**
     * 创建HTML
     *
     * @return
     */
    public String createHtml(Table tableObj, String projectName, String pkgNm, boolean fileFlag) {
        List<String> template = super.getTemplete("listPage.html");
        String fileName = "index";
        StringBuilder htmlStr = new StringBuilder();
        Map<String, String> listHtml = thForeach(tableObj);
        for (String line : template) {
            //
            if (line.indexOf(Context.COLUMN_TITLE) >= 0) {
                htmlStr.append(listHtml.get(Context.COLUMN_TITLE) + Context.CRLF);
            } else if (line.indexOf(Context.COLUMN_INFO) >= 0) {
                htmlStr.append(listHtml.get(Context.COLUMN_INFO) + Context.CRLF);
            } else {
                htmlStr.append(line + Context.CRLF);
            }
        }

        if (fileFlag) {
            super.writeFile(projectName, pkgNm + tableObj.getName().toLowerCase(), fileName + ".html",
                    htmlStr.toString());
        }
        return htmlStr.toString();
    }

    public String createDetailHtml(Table tableObj, String projectName, String pkgNm, boolean fileFlag) {
        List<String> template = super.getTemplete("detail.html");
        String fileName = "detail";
        StringBuilder htmlStr = new StringBuilder();
        Map<String, String> listHtml = thForeach(tableObj);
        for (String line : template) {
            //
            if (line.indexOf(Context.COLUMN_TITLE) >= 0) {
                htmlStr.append(listHtml.get(Context.COLUMN_TITLE) + Context.CRLF);
            } else if (line.indexOf(Context.COLUMN_INFO) >= 0) {
                htmlStr.append(listHtml.get(Context.COLUMN_INFO) + Context.CRLF);
            } else {
                htmlStr.append(line + Context.CRLF);
            }
        }

        if (fileFlag) {
            super.writeFile(projectName, pkgNm + tableObj.getName().toLowerCase(), fileName + ".html",
                    htmlStr.toString());
        }
        return htmlStr.toString();
    }

    /**
     * 初期化表示
     *
     * @param  objNm
     * @param  paramNm
     * @return
     */
    private String createMethodIndex(String objNm, String paramNm) {
        StringBuilder ctrlStr = new StringBuilder();

        ctrlStr.append("    /**" + Context.CRLF);
        ctrlStr.append("     * 初期表示" + Context.CRLF);
        ctrlStr.append("     */" + Context.CRLF);
        ctrlStr.append(editReqMp("index", false));
        ctrlStr.append("    public String goIndex(Model model) {" + Context.CRLF);
        ctrlStr.append("        " + objNm + " inDto = new " + objNm + "();" + Context.CRLF);
        ctrlStr.append("        List<" + objNm + "> resultList = " + paramNm + "Repository.selectByDto(inDto);"
                + Context.CRLF);
        ctrlStr.append("        model.addAttribute(\"resultList\", resultList);" + Context.CRLF);
        ctrlStr.append("        return \"" + paramNm + "/index\";" + Context.CRLF);
        ctrlStr.append("    }" + Context.CRLF2);

        return ctrlStr.toString();
    }

    /**
     * 页面list
     *
     * @param  tableObj
     * @return
     */
    private Map<String, String> thForeach(Table tableObj) {
        Map<String, String> result = new HashMap<String, String>();
        StringBuilder columnTitle = new StringBuilder();
        StringBuilder columnInfo = new StringBuilder();
        for (Table.Column column : tableObj.getColumnList()) {
            columnTitle.append(Context.mkSpace(12) + "<td>" + column.getLogicName() + "</td>" + Context.CRLF);
            // <td th:text="${obj.name}"></td>
            columnInfo.append(
                    Context.mkSpace(12) + "<td th:text=\"${obj." + column.getSourceName() + "}\"></td>" + Context.CRLF);
        }
        result.put(Context.COLUMN_TITLE, columnTitle.toString());
        result.put(Context.COLUMN_INFO, columnInfo.toString());
        return result;
    }

    /**
     * 检索
     *
     * @param  objNm
     * @param  paramNm
     * @return
     */
    private String createMethodSearch(String objNm, String paramNm) {
        StringBuilder ctrlStr = new StringBuilder();

        ctrlStr.append("    /**" + Context.CRLF);
        ctrlStr.append("     * 检索" + Context.CRLF);
        ctrlStr.append("     */" + Context.CRLF);
        ctrlStr.append(editReqMp("search", false));
        ctrlStr.append("    public String doAjaxSearch(Model model," + objNm + " " + paramNm + ") {" + Context.CRLF);
        ctrlStr.append("        List<" + objNm + "> resultList = " + paramNm + "Repository.selectByDto(" + paramNm
                + ");" + Context.CRLF);
        ctrlStr.append("        model.addAttribute(\"resultList\", resultList);" + Context.CRLF);
        ctrlStr.append("        return \"" + paramNm + "/index\";" + Context.CRLF);
        ctrlStr.append("    }" + Context.CRLF2);

        return ctrlStr.toString();
    }

    /**
     * AJAX检索
     *
     * @param  objNm
     * @param  paramNm
     * @return
     */
    private String createMethodAjaxSearch(String objNm, String paramNm) {
        StringBuilder ctrlStr = new StringBuilder();

        ctrlStr.append("    /**" + Context.CRLF);
        ctrlStr.append("     * AJAX检索" + Context.CRLF);
        ctrlStr.append("     */" + Context.CRLF);
        ctrlStr.append(editReqMp("ajaxSearch", false));
        ctrlStr.append("    @ResponseBody" + Context.CRLF);
        ctrlStr.append("    public Object doAjaxSearch(@RequestBody " + objNm + " " + paramNm + ") {" + Context.CRLF);
        ctrlStr.append("        List<" + objNm + "> resultList = " + paramNm + "Repository.selectByDto(" + paramNm
                + ");" + Context.CRLF);
        ctrlStr.append("        return resultList;" + Context.CRLF);
        ctrlStr.append("    }" + Context.CRLF2);

        return ctrlStr.toString();
    }

    /**
     * 取得单条数据
     *
     * @param  objNm
     * @param  paramNm
     * @return
     */
    private String createMethodDetail(String objNm, String paramNm, boolean hibernateFlag) {
        StringBuilder ctrlStr = new StringBuilder();

        ctrlStr.append("    /**" + Context.CRLF);
        ctrlStr.append("     * 取得单条数据" + Context.CRLF);
        ctrlStr.append("     */" + Context.CRLF);
        ctrlStr.append(editReqMp("detail/{id}", false));
        ctrlStr.append("    public String getDetail(@PathVariable(\"id\") String id, Model model) {" + Context.CRLF);
        if (hibernateFlag) {
            ctrlStr.append(
                    "        " + objNm + " " + paramNm + " = " + paramNm + "Dao.findById(id).get();" + Context.CRLF);
        } else {
            ctrlStr.append(
                    "        " + objNm + " " + paramNm + " = " + paramNm + "Dao.selectOneById(id);" + Context.CRLF);
        }
        ctrlStr.append("        model.addAttribute(\"detailObj\", " + paramNm + ");" + Context.CRLF);
        ctrlStr.append("        return \"" + paramNm + "/detail\";" + Context.CRLF);
        ctrlStr.append("    }" + Context.CRLF2);

        return ctrlStr.toString();
    }

    private String createMethodUpdate(String objNm, String paramNm, boolean hibernateFlag) {
        StringBuilder ctrlStr = new StringBuilder();

        ctrlStr.append("    /**" + Context.CRLF);
        ctrlStr.append("     * Ajax更新" + Context.CRLF);
        ctrlStr.append("     */" + Context.CRLF);
        ctrlStr.append(editReqMp("regist/execute", false));
        ctrlStr.append("    @ResponseBody" + Context.CRLF);
        ctrlStr.append("    public String doAjaxRegist(Model model," + objNm + " obj) {" + Context.CRLF);
        ctrlStr.append("        // 判断是更新还是新规" + Context.CRLF);
        ctrlStr.append("        String id = obj.getId();" + Context.CRLF2);
        if (!hibernateFlag) {
            ctrlStr.append("        if (id.length() > 0) {" + Context.CRLF);
            ctrlStr.append("            " + paramNm + "Dao.update(obj);" + Context.CRLF);
            ctrlStr.append("        } else {" + Context.CRLF);
            ctrlStr.append("            " + paramNm + "Dao.insert(obj);" + Context.CRLF);
            ctrlStr.append("        }" + Context.CRLF2);
        } else {
            ctrlStr.append("        " + paramNm + "Dao.save(obj);" + Context.CRLF);
        }
        ctrlStr.append("        return \"update success!\";" + Context.CRLF);
        ctrlStr.append("    }" + Context.CRLF2);

        return ctrlStr.toString();
    }

    /**
     * @param  value
     * @return       "@RequestMapping(value = "/{value}")"
     */
    private String editReqMp(String value, boolean withSlash) {
        if (withSlash)
            return "@RequestMapping(value = \"/" + value + "\")" + Context.CRLF;
        else
            return "    @RequestMapping(value = \"" + value + "\")" + Context.CRLF;
    }
}
