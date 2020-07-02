package com.pojo;

import java.util.List;

public class Table {

    private String name;

    private List<Field> fieldList;

    private String joinTableNm;

    private List<Field> joinField;

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

    public List<Field> getJoinField() {
        return joinField;
    }

    public void setJoinField(List<Field> joinField) {
        this.joinField = joinField;
    }

}
