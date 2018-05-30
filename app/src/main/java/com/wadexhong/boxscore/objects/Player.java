package com.wadexhong.boxscore.objects;

import java.io.Serializable;

/**
 * Created by wade8 on 2018/5/2.
 */

public class Player implements Serializable {

    public static final int POSITION_C = 0;
    public static final int POSITION_PF = 1;
    public static final int POSITION_SF = 2;
    public static final int POSITION_SG = 3;
    public static final int POSITION_PG = 4;

    private String mPlayerId;
    private String mNumber;
    private String mName;
    private int[] mPosition;

    public Player(String number, String name, String playerId){
        this(number,name);
        mPlayerId = playerId;
    }

    public Player(String number, String name) {
        this.mNumber = number;
        this.mName = name;
    }

    public Player(String number, String name, int[] position){
        this(number,name);
        mPosition = position;
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

    public int[] getPosition() {
        return mPosition;
    }

    public void setPosition(int[] position) {
        mPosition = position;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String playerId) {
        mPlayerId = playerId;
    }

}
