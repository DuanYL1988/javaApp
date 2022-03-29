package com.application.creater;

import java.io.File;
import java.util.List;

import com.application.util.FileUtils;
import com.application.util.PropertyUtil;

public class BaseCreater implements Creater {

    PropertyUtil propUtil = new PropertyUtil();

    public void writeFile(String project, String pkgPath, String fileName, String writeStr) {
        // 创建文件夹
        FileUtils.createFolder(pkgPath, propUtil.getParamByKey(project));

        // 生成文件
        String filePath = propUtil.getParamByKey(project);
        filePath += "\\" + pkgPath.replace(".", "\\\\") + "\\" + fileName;
        System.out.println(filePath);
        FileUtils.writeFile(filePath, writeStr);
    }

    public List<String> getTemplete(String templeteName) {
        String templatePath = FileUtils.getPath() + "\\resources\\" + templeteName;
        return FileUtils.getFileText(new File(templatePath));
    }

}
