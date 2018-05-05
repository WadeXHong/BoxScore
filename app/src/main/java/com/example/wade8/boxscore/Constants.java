package com.example.wade8.boxscore;

import android.util.SparseArray;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by wade8 on 2018/5/5.
 */

public class Constants {

    public static final SparseArray<Integer> mSpareArray = new SparseArray<Integer>(){
        {
            append(RecordDataType.TWO_POINT_SHOT,R.string.twoPointShot);
            append(RecordDataType.TWO_POINT_SHOT_MADE,R.string.twoPointShotMade);
            append(RecordDataType.TWO_POINT_SHOT_MISSED,R.string.twoPointShotMiss);
            append(RecordDataType.THREE_POINT_SHOT,R.string.threePointShot);
            append(RecordDataType.THREE_POINT_SHOT_MADE,R.string.threePointShotMade);
            append(RecordDataType.THREE_POINT_SHOT_MISSED,R.string.threePointShotMiss);
            append(RecordDataType.FREE_THROW_SHOT,R.string.freeThrowShot);
            append(RecordDataType.FREE_THROW_SHOT_MADE,R.string.freeThrowShotMade);
            append(RecordDataType.FREE_THROW_SHOT_MISSED,R.string.freeThrowShotMiss);
            append(RecordDataType.ASSIST,R.string.assist);
            append(RecordDataType.OFFENSIVE_REBOUND,R.string.offensiveRebound);
            append(RecordDataType.DEFENSIVE_REBOUND,R.string.defensiveRebound);
            append(RecordDataType.STEAL,R.string.steal);
            append(RecordDataType.BLOCK,R.string.block);
            append(RecordDataType.FOUL,R.string.foul);
            append(RecordDataType.TURNOVER,R.string.turnover);

        }
    };


    public class RecordDataType{
        public static final int TWO_POINT_SHOT = 0x01;
        public static final int TWO_POINT_SHOT_MADE = 0x02;
        public static final int TWO_POINT_SHOT_MISSED = 0x03;
        public static final int THREE_POINT_SHOT = 0x11;
        public static final int THREE_POINT_SHOT_MADE = 0x12;
        public static final int THREE_POINT_SHOT_MISSED= 0x13;
        public static final int FREE_THROW_SHOT = 0x21;
        public static final int FREE_THROW_SHOT_MADE = 0x22;
        public static final int FREE_THROW_SHOT_MISSED = 0x23;
        public static final int ASSIST = 0x31;
        public static final int OFFENSIVE_REBOUND = 0x41;
        public static final int DEFENSIVE_REBOUND = 0x51;
        public static final int STEAL = 0x61;
        public static final int BLOCK = 0x71;
        public static final int FOUL= 0x81;
        public static final int TURNOVER = 0x91;

        public static final int MINUS_TWO_POINT_MISSED = 0x04;
        public static final int MINUS_TWO_POINT_GOAL = 0x05;
        public static final int MINUS_THREE_POINT_MISSED = 0x14;
        public static final int MINUS_THREE_POINT_GOAL= 0x15;
        public static final int MINUS_FREE_THROW_MISSED = 0x24;
        public static final int MINUS_FREE_THROW_= 0x25;
        public static final int MINUS_ASSIST = 0x32;
        public static final int MINUS_OFFENSIVE_REBOUND = 0x42;
        public static final int MINUS_DEFENSIVE_REBOUND = 0x52;
        public static final int MINUS_STEAL = 0x62;
        public static final int MINUS_BLOCK = 0x72;
        public static final int MINUS_FOUL= 0x82;
        public static final int MINUS_TURNOVER = 0x92;

    }





}
