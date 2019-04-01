package com.bardxhong.boxscore.objects;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameInfo implements Serializable {

    private String mGameId;
    private String mGameName;
    private String mOpponentName;
    private String mGameDate;
    private String mYourTeamId;
    private String mYourTeam;
    private String mQuarterLength;
    private String mTotalQuarter;
    private String mMaxFoul;
    private String mTimeoutFirstHalf;
    private String mTimeoutSecondHalf;
    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;
    private SparseArray<SparseArray<SparseIntArray>> mDetailData;
    private SparseIntArray mTeamData;

    public GameInfo() {

        mGameId = "";
        mGameName = "";
        mOpponentName = "";
        mGameDate = "";
        mYourTeam = "";
        mYourTeamId = "";
        mQuarterLength = "";
        mTotalQuarter = "";
        mMaxFoul = "";
        mTimeoutFirstHalf = "";
        mTimeoutSecondHalf = "";

    }

    public String getGameId() {
        return mGameId;
    }

    public void setGameId(String gameId) {
        mGameId = gameId;
    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public void setOpponentName(String opponentName) {
        mOpponentName = opponentName;
    }

    public String getGameDate() {
        return mGameDate;
    }

    public void setGameDate(String gameDate) {
        mGameDate = gameDate;
    }

    public String getYourTeam() {
        return mYourTeam;
    }

    public void setYourTeam(String yourTeam) {
        mYourTeam = yourTeam;
    }

    public String getQuarterLength() {
        return mQuarterLength;
    }

    public void setQuarterLength(String quarterLength) {
        mQuarterLength = quarterLength;
    }

    public String getTotalQuarter() {
        return mTotalQuarter;
    }

    public void setTotalQuarter(String totalQuarter) {
        mTotalQuarter = totalQuarter;
    }

    public String getMaxFoul() {
        return mMaxFoul;
    }

    public void setMaxFoul(String maxFoul) {
        mMaxFoul = maxFoul;
    }

    public String getTimeoutFirstHalf() {
        return mTimeoutFirstHalf;
    }

    public void setTimeoutFirstHalf(String timeoutFirstHalf) {
        mTimeoutFirstHalf = timeoutFirstHalf;
    }

    public String getTimeoutSecondHalf() {
        return mTimeoutSecondHalf;
    }

    public void setTimeoutSecondHalf(String timeoutSecondHalf) {
        mTimeoutSecondHalf = timeoutSecondHalf;
    }

    public ArrayList<Player> getStartingPlayerList() {
        return mStartingPlayerList;
    }

    public void setStartingPlayerList(ArrayList<Player> startingPlayerList) {
        mStartingPlayerList = startingPlayerList;
    }

    public ArrayList<Player> getSubstitutePlayerList() {
        return mSubstitutePlayerList;
    }

    public void setSubstitutePlayerList(ArrayList<Player> substitutePlayerList) {
        mSubstitutePlayerList = substitutePlayerList;
    }


    public SparseArray<SparseArray<SparseIntArray>> getDetailData() {
        return mDetailData;
    }

    public void setDetailData(SparseArray<SparseArray<SparseIntArray>> detailData) {
        mDetailData = detailData;
    }

    public SparseIntArray getTeamData() {
        return mTeamData;
    }

    public void setTeamData(SparseIntArray teamData) {
        mTeamData = teamData;
    }

    public String getYourTeamId() {
        return mYourTeamId;
    }

    public void setYourTeamId(String yourTeamId) {
        mYourTeamId = yourTeamId;
    }
}
