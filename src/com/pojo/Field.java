package com.pojo;

public class Field {

    private String dbNm;

    private String javaNm;

    private String logicNm;

    private String dbType;

    private String javaType;

    private Integer size;

    private boolean primaryKey;

    private boolean existColumn;

    private String prefixValue;

    public String getDbNm() {
        return dbNm;
    }

    public void setDbNm(String dbNm) {
        this.dbNm = dbNm;
    }

    public String getJavaNm() {
        return javaNm;
    }

    public void setJavaNm(String javaNm) {
        this.javaNm = javaNm;
    }

    public String getLogicNm() {
        return logicNm;
    }

    public void setLogicNm(String logicNm) {
        this.logicNm = logicNm;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isExistColumn() {
        return existColumn;
    }

    public void setExistColumn(boolean existColumn) {
        this.existColumn = existColumn;
    }

    public String getPrefixValue() {
        return prefixValue;
    }

    public void setPrefixValue(String prefixValue) {
        this.prefixValue = prefixValue;
    }


}
