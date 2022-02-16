package com.application.dto;

import java.util.List;

public class Table {

    private String databaseName;

    private String name;

    private Column pkey;

    private List<Column> uniqueKey;

    private List<Column> columnList;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Column getPkey() {
        return pkey;
    }

    public void setPkey(Column pkey) {
        this.pkey = pkey;
    }

    public List<Column> getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(List<Column> uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public Column getColumnInstance() {
        return new Column();
    }

    /**
     * ���ݿ��ֶζ���
     *
     * @author dylsw
     *
     */
    public class Column {
        private String sourceName;

        private String logicName;

        private String columnName;

        private String javaType;

        private String columnType;

        private Integer length;

        private String div;

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getLogicName() {
            return logicName;
        }

        public void setLogicName(String logicName) {
            this.logicName = logicName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public String getDiv() {
            return div;
        }

        public void setDiv(String div) {
            this.div = div;
        }

    }

}
