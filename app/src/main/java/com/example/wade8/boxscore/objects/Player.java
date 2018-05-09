package com.example.wade8.boxscore.objects;

import java.io.Serializable;

/**
 * Created by wade8 on 2018/5/2.
 */

public class Player implements Serializable {

    private String mNumber;
    private String mName;

    public Player(String mNumber, String mName) {
        this.mNumber = mNumber;
        this.mName = mName;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

}
