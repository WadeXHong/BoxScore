package com.example.wade8.boxscore;

import android.provider.BaseColumns;
import android.util.SparseArray;

/**
 * Created by wade8 on 2018/5/5.
 */

public class Constants {

    public static final SparseArray<Integer> TITLE_SPARSE_ARRAY = new SparseArray<Integer>(){
        {
            append(RecordDataType.POINTS,R.string.points);
            append(RecordDataType.TWO_POINT_SHOT,R.string.twoPointShot);
            append(RecordDataType.TWO_POINT_SHOT_MADE,R.string.twoPointShotMade);
            append(RecordDataType.TWO_POINT_SHOT_MISSED,R.string.twoPointShotMiss);
            append(RecordDataType.THREE_POINT_SHOT,R.string.threePointShot);
            append(RecordDataType.THREE_POINT_SHOT_MADE,R.string.threePointShotMade);
            append(RecordDataType.THREE_POINT_SHOT_MISSED,R.string.threePointShotMiss);
            append(RecordDataType.FREE_THROW_SHOT,R.string.freeThrowShot);
            append(RecordDataType.FREE_THROW_SHOT_MADE,R.string.freeThrowShotMade);
            append(RecordDataType.FREE_THROW_SHOT_MISSED,R.string.freeThrowShotMiss);
            append(RecordDataType.OFFENSIVE_REBOUND,R.string.offensiveRebound);
            append(RecordDataType.DEFENSIVE_REBOUND,R.string.defensiveRebound);
            append(RecordDataType.ASSIST,R.string.assist);
            append(RecordDataType.STEAL,R.string.steal);
            append(RecordDataType.BLOCK,R.string.block);
            append(RecordDataType.FOUL,R.string.foul);
            append(RecordDataType.TURNOVER,R.string.turnover);

        }
    };

    public static final SparseArray<String> COLUMN_NAME_SPARSE_ARRAY = new SparseArray<String>(){
        {
            append(RecordDataType.POINTS, GameDataDBContract.COLUMN_NAME_POINTS);
            append(RecordDataType.TWO_POINT_SHOT_MADE,GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE);
            append(RecordDataType.TWO_POINT_SHOT_MISSED,GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED);
            append(RecordDataType.THREE_POINT_SHOT_MADE,GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE);
            append(RecordDataType.THREE_POINT_SHOT_MISSED,GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED);
            append(RecordDataType.FREE_THROW_SHOT_MADE,GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE);
            append(RecordDataType.FREE_THROW_SHOT_MISSED,GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED);
            append(RecordDataType.OFFENSIVE_REBOUND,GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND);
            append(RecordDataType.DEFENSIVE_REBOUND,GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND);
            append(RecordDataType.ASSIST,GameDataDBContract.COLUMN_NAME_ASSIST);
            append(RecordDataType.STEAL,GameDataDBContract.COLUMN_NAME_STEAL);
            append(RecordDataType.BLOCK,GameDataDBContract.COLUMN_NAME_BLOCK);
            append(RecordDataType.FOUL,GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL);
            append(RecordDataType.TURNOVER,GameDataDBContract.COLUMN_NAME_TURNOVER);
        }
    };


    public class RecordDataType{
        public static final int YOUR_TEAM_TOTAL_SCORE = 0xa1;
        public static final int OPPONENT_TEAM_TOTAL_SCORE = 0xb1;
        public static final int YOUR_TEAM_FOUL = 0xc1;
        public static final int OPPONENT_TEAM_FOUL = 0xd1;
        public static final int QUARTER = 0xe1;
        public static final int MINUS_YOUR_TEAM_TOTAL_SCORE = 0xa2;
        public static final int MINUS_OPPONENT_TEAM_TOTAL_SCORE = 0xb2;
        public static final int MINUS_YOUR_TEAM_TEAM_FOUL = 0xc2;
        public static final int MINUS_OPPONENT_TEAM_TOTAL_FOUL = 0xd2;
        public static final int MINUS_QUARTER = 0xe2;


        public static final int POINTS = 0x00;

        public static final int TWO_POINT_SHOT = 0x01;
        public static final int TWO_POINT_SHOT_MADE = 0x02;
        public static final int TWO_POINT_SHOT_MISSED = 0x03;
        public static final int THREE_POINT_SHOT = 0x11;
        public static final int THREE_POINT_SHOT_MADE = 0x12;
        public static final int THREE_POINT_SHOT_MISSED= 0x13;
        public static final int FREE_THROW_SHOT = 0x21;
        public static final int FREE_THROW_SHOT_MADE = 0x22;
        public static final int FREE_THROW_SHOT_MISSED = 0x23;
        public static final int OFFENSIVE_REBOUND = 0x31;
        public static final int DEFENSIVE_REBOUND = 0x41;
        public static final int ASSIST = 0x51;
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
        public static final int MINUS_OFFENSIVE_REBOUND = 0x32;
        public static final int MINUS_DEFENSIVE_REBOUND = 0x42;
        public static final int MINUS_ASSIST = 0x52;
        public static final int MINUS_STEAL = 0x62;
        public static final int MINUS_BLOCK = 0x72;
        public static final int MINUS_FOUL= 0x82;
        public static final int MINUS_TURNOVER = 0x92;

    }

    public class GameDataDBContract implements BaseColumns{
        public static final String _ID = "_id";
        public static final String TABLE_NAME = "game_data";
        public static final String COLUMN_NAME_GAME_ID = "game_id";
        public static final String COLUMN_NAME_QUARTER = "quarter";
        public static final String COLUMN_NAME_PLAYER_NUMBER = "player_number";
        public static final String COLUMN_NAME_PLAYER_NAME = "player_name";
        public static final String COLUMN_NAME_POINTS = "PTS";
        public static final String COLUMN_NAME_FIELD_GOALS_MADE = "FGM";
        public static final String COLUMN_NAME_FIELD_GOALS_ATTEMPTED = "FGA";
        public static final String COLUMN_NAME_THREE_POINT_MADE = "TPM";
        public static final String COLUMN_NAME_THREE_POINT_ATTEMPTED = "TPA";
        public static final String COLUMN_NAME_FREE_THROW_MADE = "FTM";
        public static final String COLUMN_NAME_FREE_THROW_ATTEMPTED = "FTA";
        public static final String COLUMN_NAME_OFFENSIVE_REBOUND = "OREB";
        public static final String COLUMN_NAME_DEFENSIVE_REBOUND = "DREB";
        public static final String COLUMN_NAME_ASSIST = "AST";
        public static final String COLUMN_NAME_STEAL = "STL";
        public static final String COLUMN_NAME_BLOCK = "BLK";
        public static final String COLUMN_NAME_PERSONAL_FOUL= "PF";
        public static final String COLUMN_NAME_TURNOVER = "TOV";
    }



}
