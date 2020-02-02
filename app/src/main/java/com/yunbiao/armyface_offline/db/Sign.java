package com.yunbiao.armyface_offline.db;

import android.graphics.Bitmap;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Sign {
    @Id
    private Long id;

    private String userCode;

    private String userName;

    private String signIcon;

    private String icon;

    private String signType;

    private String companyCode;

    private String companyName;

    private String deptCode;

    private String icNo;

    private String startTime;

    private String endTime;

    private String date;

    private String device_code;

    private long time;

    private String status;

    @Generated(hash = 870883153)
    public Sign(Long id, String userCode, String userName, String signIcon,
            String icon, String signType, String companyCode, String companyName,
            String deptCode, String icNo, String startTime, String endTime,
            String date, String device_code, long time, String status) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.signIcon = signIcon;
        this.icon = icon;
        this.signType = signType;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.deptCode = deptCode;
        this.icNo = icNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.device_code = device_code;
        this.time = time;
        this.status = status;
    }

    @Generated(hash = 2025164192)
    public Sign() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getSignIcon() {
        return this.signIcon;
    }

    public void setSignIcon(String signIcon) {
        this.signIcon = signIcon;
    }

    public String getSignType() {
        return this.signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
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

    public String getIcNo() {
        return this.icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Sign{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", signIcon='" + signIcon + '\'' +
                ", signType='" + signType + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", deptCode='" + deptCode + '\'' +
                ", icNo='" + icNo + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", date='" + date + '\'' +
                ", device_code='" + device_code + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }
}
