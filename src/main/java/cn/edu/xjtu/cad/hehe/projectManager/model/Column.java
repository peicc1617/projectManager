package cn.edu.xjtu.cad.hehe.projectManager.model;

import java.util.Objects;
import java.util.Set;

public class Column {


    /**
     * 字段名称
     */
    String Field;
    /**
     * 字段类型
     */
    String Type;
    /**
     * 是否为空，一般为YES，NO 两个值
     */
    String Null;
    /**
     * 是否为主键
     */
    String Key;
    /**
     * 默认值是多少，对于text系列类型的字段，默认值为空的话为null
     * 其他为空的话"NULL"
     */
    String Default;
    String Extra;
    String Comment;

    public Column() {
    }

    public Column(String field, String type, String aNull, String key, String aDefault, String extra,String comment) {
        Field = field;
        Type = type;
        Null = aNull;
        Key = key;
        Default = aDefault;
        Extra = extra;
        Comment = comment;
    }


    public String getField() {
        return Field;
    }

    public void setField(String Field) {
        this.Field = Field;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNull() {
        return Null;
    }

    public void setNull(String aNull) {
        Null = aNull;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }
    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getSQLString(){
        StringBuilder sb = new StringBuilder(Field).append(' ').append(Type).append(' ');
        if(Null.equals("NO")){
            sb.append("NOT NULL ");
        }
        if(Default==null){
            if(!Type.contains("text")&&Null.equals("YES")){
                sb.append("DEFAULT NULL ");
            }
        }else {
            sb.append("DEFAULT ").append(Default).append(' ');
        }
        return sb.append(Extra).append(" COMMENT '").append(Comment).append('\'').toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(Field, column.Field) &&
                Objects.equals(Type, column.Type) &&
                Objects.equals(Null, column.Null) &&
                Objects.equals(Key, column.Key) &&
                Objects.equals(Default, column.Default) &&
                Objects.equals(Extra, column.Extra) &&
                Objects.equals(Comment, column.Comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Field, Type, Null, Key, Default, Extra, Comment);
    }

}
