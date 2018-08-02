package com.wadexhong.boxscore.objects;

/**
 * Created by wade8 on 2018/5/9.
 */

public class Undo {

    private Player mPlayer;
    private int mType;
    private int mQuarter;
    private boolean mIsMarked;

    public Undo(int type, int quarter, Player player) {
        mPlayer = player;
        mType = type;
        mQuarter = quarter;
    }

    public int getQuarter() {
        return mQuarter;
    }

    public void setQuarter(int quarter) {
        mQuarter = quarter;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayer(Player player) {
        mPlayer = player;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isMarked() {
        return mIsMarked;
    }

    public void setMarked(boolean marked) {
        mIsMarked = marked;
    }
}
