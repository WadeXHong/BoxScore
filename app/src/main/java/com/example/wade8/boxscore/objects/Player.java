package com.example.wade8.boxscore.objects;

/**
 * Created by wade8 on 2018/5/2.
 */

public class Player {

    private String mNumber;
    private String mName;

    public Player(String mNumber, String mName) {
        this.mNumber = mNumber;
        this.mName = mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

}
