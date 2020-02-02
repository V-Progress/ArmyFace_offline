package com.yunbiao.armyface_offline.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    private Long id;

    @Unique
    private String userCode;

    private String userName;

    private String companyCode;
    private String companyName;

    private String deptCode;
    private String deptName;

    private String icNo;

    private String icon;

    private String userStatus;

    private String updateTime;


    @Generated(hash = 616671118)
    public User(Long id, String userCode, String userName, String companyCode,
            String companyName, String deptCode, String deptName, String icNo,
            String icon, String userStatus, String updateTime) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.icNo = icNo;
        this.icon = icon;
        this.userStatus = userStatus;
        this.updateTime = updateTime;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyCode() {
        return this.companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getIcNo() {
        return this.icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", icNo='" + icNo + '\'' +
                ", icon='" + icon + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
