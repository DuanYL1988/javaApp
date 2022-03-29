package com.application.creater;

import java.util.List;

public interface Creater {

    void writeFile(String project, String pkgPath, String fileName, String writeStr);

    List<String> getTemplete(String templeteName);
}
