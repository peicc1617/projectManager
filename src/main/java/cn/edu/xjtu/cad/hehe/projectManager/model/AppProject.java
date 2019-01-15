package cn.edu.xjtu.cad.hehe.projectManager.model;

import java.util.Date;

public class AppProject {
    /**
     * 项目id
     */
    private int id;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户名
     */
    private long userID;
    /**
     * 备注
     */
    private String memo;
    /**
     * 项目结果，特指word片段
     */
    private String appResult;

    /**
     * 预留内容字段
     */
    private String appContent;
    /**
     * 预留内容字段2
     */
    private String reservation;

    private String resultKey;

    private Date editTime;

    public AppProject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAppResult() {
        return appResult;
    }

    public void setAppResult(String appResult) {
        this.appResult = appResult;
    }

    public String getAppContent() {
        return appContent;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}
