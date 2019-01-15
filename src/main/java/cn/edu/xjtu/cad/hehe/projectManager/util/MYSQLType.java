package cn.edu.xjtu.cad.hehe.projectManager.util;

import java.math.BigDecimal;
import java.util.Date;

public enum  MYSQLType {
    BIGINT(Long.class),
    TINYINT(Byte.class),
    SMALLINT(Short.class),
    MEDIUMINT(Integer.class),
    INTEGER(Integer.class),
    INT(Integer.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DECIMAL(BigDecimal.class),
    NUMERIC(BigDecimal.class),
    CHAR(String.class),
    VARCHAR(String.class),
    TINYBLOB(byte[].class),
    TINYTEXT(String.class),
    BLOB(byte[].class),
    TEXT(String.class),
    MEDIUMBLOB(byte[].class),
    MEDIUMTEXT(String.class),
    LONGBLOB(byte[].class),
    LONGTEXT(String.class),
    DATE(Date.class),
    TIME(Date.class),
    YEAR(Date.class),
    DATETIME(Date.class),
    TIMESTAMP(Date.class);

    private final Class javaClass;

    MYSQLType(Class javaClass) {
        this.javaClass = javaClass;
    }

    public Class getJavaClass(){
        return this.javaClass;
    }
}
