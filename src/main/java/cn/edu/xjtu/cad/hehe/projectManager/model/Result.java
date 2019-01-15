package cn.edu.xjtu.cad.hehe.projectManager.model;

import com.sun.org.apache.regexp.internal.RE;

public class Result {
    private boolean state =false;
    private String error;
    private Object content;

    public Result() {
    }

    public Result(boolean state, String error, Object content) {
        this.state = state;
        this.error = error;
        this.content = content;
    }

    public static Result success(Object object){
        return new Result(true,null,object);
    }

    public static Result failure(String msg){
        return new Result(false,msg,null);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
