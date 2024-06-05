package com.netease.nim.uikit.business.event;

import java.util.ArrayList;

public class VideoCallEvent {
    private String teamId, roomName, teamName,teamNick,headUrl;
    private ArrayList<String> accounts;

    public String getTeamId() {
        return teamId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamNick() {
        return teamNick;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public ArrayList<String> getAccounts() {
        return accounts;
    }

    public VideoCallEvent(String teamId, String roomName, ArrayList<String> accounts, String teamName,
                          String teamNick, String headUrl) {
        this.teamId = teamId;
        this.roomName = roomName;
        this.teamName = teamName;
        this.teamNick = teamNick;
        this.headUrl = headUrl;
        this.accounts = accounts;
    }

}
