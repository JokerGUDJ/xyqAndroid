package com.xej.xhjy.bean;

public class XHMeeting {
    public String getCreateAccId() {
        return createAccId;
    }

    public void setCreateAccId(String createAccId) {
        this.createAccId = createAccId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeetFrom() {
        return meetFrom;
    }

    public void setMeetFrom(String meetFrom) {
        this.meetFrom = meetFrom;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getWhetherJoin() {
        return whetherJoin;
    }

    public void setWhetherJoin(String whetherJoin) {
        this.whetherJoin = whetherJoin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getSistt() {
        return sistt;
    }

    public void setSisst(String sistt) {
        this.sistt = sistt;
    }


    String name;
    String createAccId;
    String id;
    String meetFrom; //S,线上会议，M，线下会议
    String startTime;
    String stt; //会议状态 U:未结束，W:进行中，S:已结束
    String whetherJoin; //是否报名
    String address; //线下会议地址
    String beginDate;//会议开始时间
    String endDate;//会议结束时间
    String sistt; //whetherJoin为Y是有此字段，0,未签到，1，已签到
}
