package com.wadexhong.boxscore.objects;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/3.
 */

public class GameInfo implements Serializable{
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

    public String getGameId() {
        return mGameId;
    }

    public void setGameId(String mGameId) {
        this.mGameId = mGameId;
    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String mGameName) {
        this.mGameName = mGameName;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public void setOpponentName(String mOpponentName) {
        this.mOpponentName = mOpponentName;
    }

    public String getGameDate() {
        return mGameDate;
    }

    public void setGameDate(String mGameDate) {
        this.mGameDate = mGameDate;
    }

    public String getYourTeam() {
        return mYourTeam;
    }

    public void setYourTeam(String mYourTeam) {
        this.mYourTeam = mYourTeam;
    }

    public String getQuarterLength() {
        return mQuarterLength;
    }

    public void setQuarterLength(String mQuarterLength) {
        this.mQuarterLength = mQuarterLength;
    }

    public String getTotalQuarter() {
        return mTotalQuarter;
    }

    public void setTotalQuarter(String mTotalQuarter) {
        this.mTotalQuarter = mTotalQuarter;
    }

    public String getMaxFoul() {
        return mMaxFoul;
    }

    public void setMaxFoul(String mMaxFoul) {
        this.mMaxFoul = mMaxFoul;
    }

    public String getTimeoutFirstHalf() {
        return mTimeoutFirstHalf;
    }

    public void setTimeoutFirstHalf(String mTimeoutFirstHalf) {
        this.mTimeoutFirstHalf = mTimeoutFirstHalf;
    }

    public String getTimeoutSecondHalf() {
        return mTimeoutSecondHalf;
    }

    public void setTimeoutSecondHalf(String mTimeoutSecondHalf) {
        this.mTimeoutSecondHalf = mTimeoutSecondHalf;
    }

    public ArrayList<Player> getStartingPlayerList() {
        return mStartingPlayerList;
    }

    public void setStartingPlayerList(ArrayList<Player> mStartingPlayerList) {
        this.mStartingPlayerList = mStartingPlayerList;
    }

    public ArrayList<Player> getSubstitutePlayerList() {
        return mSubstitutePlayerList;
    }

    public void setSubstitutePlayerList(ArrayList<Player> mSubstitutePlayerList) {
        this.mSubstitutePlayerList = mSubstitutePlayerList;
    }

    public GameInfo() {

        mGameId = "";
        mGameName = "" ;
        mOpponentName = "" ;
        mGameDate = "" ;
        mYourTeam = "" ;
        mYourTeamId = "" ;
        mQuarterLength = "" ;
        mTotalQuarter = "" ;
        mMaxFoul = "" ;
        mTimeoutFirstHalf = "" ;
        mTimeoutSecondHalf = "" ;

    }


    public SparseArray<SparseArray<SparseIntArray>> getDetailData() {
        return mDetailData;
    }

    public void setDetailData(SparseArray<SparseArray<SparseIntArray>> mDetailData) {
        this.mDetailData = mDetailData;
    }

    public SparseIntArray getTeamData() {
        return mTeamData;
    }

    public void setTeamData(SparseIntArray mTeamData) {
        this.mTeamData = mTeamData;
    }

    public String getYourTeamId() {
        return mYourTeamId;
    }

    public void setYourTeamId(String yourTeamId) {
        mYourTeamId = yourTeamId;
    }
}
