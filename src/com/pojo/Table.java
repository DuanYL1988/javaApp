package com.pojo;

import java.util.List;
import java.util.Map;

public class Table {

    private String name;

    private List<Field> fieldList;

    private String joinTableNm;

    private Map<String,Field> fieldMap;

    private String primaryKeys;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public String getJoinTableNm() {
        return joinTableNm;
    }

    public void setJoinTableNm(String joinTableNm) {
        this.joinTableNm = joinTableNm;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(String primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

}
