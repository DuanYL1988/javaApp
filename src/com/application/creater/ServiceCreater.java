package com.application.creater;

import com.application.common.Context;
import com.application.dto.Table;
import com.application.util.TextUtil;

public class ServiceCreater extends BaseCreater {

    /**
     * 创建Service类
     *
     * @param  tableObj
     * @param  pkgNm
     * @return
     */
    public String createService(Table tableObj, String projectName, String pkgNm, boolean fileFlag) {

        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        // 实体类变量名
        String varNm = modelNm.toLowerCase();
        String fileNm = modelNm + "Service";

        StringBuilder serviceStr = new StringBuilder();
        // Package
        String thisPkg = pkgNm + ".service";
        serviceStr.append("package " + thisPkg + ";" + Context.CRLF2);

        // 关联对象import
        serviceStr.append("import java.util.*;" + Context.CRLF);
        serviceStr.append("import com.springboot.demo.dto.Layout;" + Context.CRLF);
        serviceStr.append("import " + pkgNm + ".model." + modelNm + ";" + Context.CRLF2);

        // Class-Start
        serviceStr.append("public interface " + fileNm + " {" + Context.CRLF2);

        // 方法
        serviceStr.append("    List<" + modelNm + "> getSearchList(" + modelNm + " " + varNm + ");" + Context.CRLF2);
        serviceStr.append("    Map<String,List<Layout>> detailLayout(String id);" + Context.CRLF2);

        // Class-end
        serviceStr.append("}" + Context.CRLF);

        if (fileFlag) {
            super.writeFile(projectName, thisPkg, fileNm + ".java", serviceStr.toString());
        }

        return serviceStr.toString();
    }

    /**
     * 创建Service类
     *
     * @param  tableObj
     * @param  pkgNm
     * @return
     */
    public String createServiceImpl(Table tableObj, String projectName, String pkgNm, boolean fileFlag,
            boolean hibernateFlag) {

        // 实体类
        String modelNm = TextUtil.transNmDbToJava(tableObj.getName(), true);
        // 实体类变量名
        String varNm = modelNm.toLowerCase();
        String fileNm = modelNm + "ServiceImpl";

        StringBuilder serviceStr = new StringBuilder();
        String thisPkg = pkgNm + ".service.impl";
        // Package
        serviceStr.append("package " + thisPkg + ";" + Context.CRLF2);

        // 关联对象import
        serviceStr.append("import java.util.*;" + Context.CRLF);

        serviceStr.append("import org.springframework.beans.factory.annotation.Autowired;" + Context.CRLF);
        serviceStr.append("import org.springframework.stereotype.Service;" + Context.CRLF2);

        serviceStr.append("import " + pkgNm + ".service." + modelNm + "Service;" + Context.CRLF);
        serviceStr.append("import " + pkgNm + ".dao." + modelNm + "Dao;" + Context.CRLF);
        if (hibernateFlag) {
            serviceStr.append("import " + pkgNm + ".dao." + modelNm + "Repository;" + Context.CRLF);
        }
        serviceStr.append("import com.springboot.demo.dto.Layout;" + Context.CRLF);
        serviceStr.append("import " + pkgNm + ".model." + modelNm + ";" + Context.CRLF2);

        serviceStr.append("@Service" + Context.CRLF);
        serviceStr.append("public class " + fileNm + " implements " + modelNm + "Service {" + Context.CRLF2);

        serviceStr.append("    @Autowired" + Context.CRLF);
        serviceStr.append("    " + modelNm + "Dao " + varNm + "Dao;" + Context.CRLF2);
        if (hibernateFlag) {
            serviceStr.append("    @Autowired" + Context.CRLF);
            serviceStr.append("    " + modelNm + "Repository " + varNm + "Repository;" + Context.CRLF2);
        }

        // 方法
        serviceStr.append(
                "    public List<" + modelNm + "> getSearchList(" + modelNm + " " + varNm + ") {" + Context.CRLF);
        serviceStr
                .append("        List<" + modelNm + "> resultList = new ArrayList<" + modelNm + ">();" + Context.CRLF2);

        serviceStr.append("        return resultList;" + Context.CRLF);
        serviceStr.append("    }" + Context.CRLF2);

        // 详细页面布局
        serviceStr.append("    public Map<String,List<Layout>> detailLayout(String id) {" + Context.CRLF);
        serviceStr.append(
                Context.SPACE8 + modelNm + " " + varNm + " = " + varNm + "Dao.findById(id).get();" + Context.CRLF);
        serviceStr.append("        Map<String,List<Layout>> layout = new HashMap<>();" + Context.CRLF);
        serviceStr.append("        List<Layout> baseInfo = new ArrayList<>();" + Context.CRLF);
        // serviceStr.append(" baseInfo.add(createCell("","",servant.getName(),""));"+ Context.CRLF2);
        String obj = TextUtil.transNmDbToJava(tableObj.getName(), false);
        for (Table.Column column : tableObj.getColumnList()) {
            if ("id".equals(column.getColumnName().toLowerCase())) {
                continue;
            }
            String getMet = TextUtil.transNmDbToJava(column.getColumnName(), true);
            String cell = "        baseInfo.add(createCell(\"";
            cell += column.getLogicName() + "\",\"";
            cell += column.getSourceName() + "\",";
            cell += obj + ".get" + getMet + "()+\"\",\"\"));";
            serviceStr.append(cell + Context.CRLF);
        }

        serviceStr.append("        layout.put(\"baseInfo\",baseInfo);" + Context.CRLF);
        serviceStr.append("        return layout;" + Context.CRLF);
        serviceStr.append("    }" + Context.CRLF2);

        serviceStr.append(
                "    private Layout createCell(String label,String name,String value,String type){" + Context.CRLF);
        serviceStr.append("        Layout layout = new Layout();" + Context.CRLF);
        serviceStr.append("        layout.setLabel(label);" + Context.CRLF);
        serviceStr.append("        layout.setName(name);" + Context.CRLF);
        serviceStr.append("        layout.setValue(value);" + Context.CRLF);
        serviceStr.append("        layout.setType(type);" + Context.CRLF);
        serviceStr.append("        return layout;" + Context.CRLF);
        serviceStr.append("    }" + Context.CRLF2);
        // Class-end
        serviceStr.append("}" + Context.CRLF);
        if (fileFlag) {
            super.writeFile(projectName, thisPkg, fileNm + ".java", serviceStr.toString());
        }
        return serviceStr.toString();
    }
}
