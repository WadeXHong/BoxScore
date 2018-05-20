package com.example.wade8.boxscore.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wade8 on 2018/5/20.
 */

public class TeamDetail implements Parcelable {

    private String mTeamName;
    private String mTeamId;
    private int mTeamPlayersAmount;
    private int mTeamHistoryAmount;

    public TeamDetail(String teamName, String teamId, int teamPlayersAmountAmount, int teamHistoryAmount){
        mTeamName = teamName;
        mTeamId = teamId;
        mTeamPlayersAmount = teamPlayersAmountAmount;
        mTeamHistoryAmount = teamHistoryAmount;
    }

    protected TeamDetail(Parcel in) {
    }

    public static final Creator<TeamDetail> CREATOR = new Creator<TeamDetail>() {
        @Override
        public TeamDetail createFromParcel(Parcel in) {
            return new TeamDetail(in);
        }

        @Override
        public TeamDetail[] newArray(int size) {
            return new TeamDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }

    public int getTeamPlayersAmount() {
        return mTeamPlayersAmount;
    }

    public void setTeamPlayersAmount(int teamPlayersAmount) {
        mTeamPlayersAmount = teamPlayersAmount;
    }

    public int getTeamHistoryAmount() {
        return mTeamHistoryAmount;
    }

    public void setTeamHistoryAmount(int teamHistoryAmount) {
        mTeamHistoryAmount = teamHistoryAmount;
    }




}
