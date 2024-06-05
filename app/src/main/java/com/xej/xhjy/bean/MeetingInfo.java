package com.xej.xhjy.bean;

import org.json.JSONArray;

public class MeetingInfo {

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getReserveStartTime() {
        return reserveStartTime;
    }

    public void setReserveStartTime(String reserveStartTime) {
        this.reserveStartTime = reserveStartTime;
    }

    public String getReserveEndTime() {
        return reserveEndTime;
    }

    public void setReserveEndTime(String reserveEndTime) {
        this.reserveEndTime = reserveEndTime;
    }

    public String getLastApplyDate() {
        return lastApplyDate;
    }

    public void setLastApplyDate(String lastApplyDate) {
        this.lastApplyDate = lastApplyDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSpeakAccName() {
        return speakAccName;
    }

    public void setSpeakAccName(String speakAccName) {
        this.speakAccName = speakAccName;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getWhetherVoice() {
        return whetherVoice;
    }

    public void setWhetherVoice(String whetherVoice) {
        this.whetherVoice = whetherVoice;
    }

    public String getWhetherVideo() {
        return whetherVideo;
    }

    public void setWhetherVideo(String whetherVideo) {
        this.whetherVideo = whetherVideo;
    }

    public String getMeetType() {
        return meetType;
    }

    public void setMeetType(String meetType) {
        this.meetType = meetType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    public String getWhetherJoin() {
        return whetherJoin;
    }

    public void setWhetherJoin(String whetherJoin) {
        this.whetherJoin = whetherJoin;
    }



    String id;//网络家园会议id
    String meetType;//1,快速，2，预约
    String meetingId;
    String meetName;
    String reserveStartTime;//会议开始时间
    String reserveEndTime; //会议结束时间
    String lastApplyDate; //报名截至时间
    String ownerName; //主持人
    String ownerId;//会议创建人id
    String speakAccName;
    String questionUrl; //调查问卷
    String member; //参会成员
    String whetherVoice;//0,不允许，1，允许
    String whetherVideo;//0,不允许，1，允许
    String status;//NOT_START(1, "未开始"),IN_MEETING(2, "进行中"),ENDED(3, "已终止"),CANCELED(4, "已取消"),RECYCLED(5, "已回收"),
    String whetherJoin;//Y,已加入，N，未加入
}
