package com.wadexhong.boxscore.objects;

/**
 * Created by wade8 on 2018/5/9.
 */

public class Undo {

    private Player mPlayer;
    private int mType;
    private int mQuarter;
    private boolean mIsMarked;

    public Undo(int mType,int mQuarter, Player mPlayer) {
        this.mPlayer = mPlayer;
        this.mType = mType;
        this.mQuarter = mQuarter;
    }

    public int getQuarter() {
        return mQuarter;
    }

    public void setQuarter(int mQuarter) {
        this.mQuarter = mQuarter;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public boolean isMarked() {
        return mIsMarked;
    }

    public void setMarked(boolean marked) {
        mIsMarked = marked;
    }
}
