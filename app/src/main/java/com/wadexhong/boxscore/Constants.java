package com.wadexhong.boxscore;

import android.provider.BaseColumns;
import android.util.SparseArray;

import com.wadexhong.boxscore.R;

/**
 * Created by wade8 on 2018/5/5.
 */

public class Constants {

    public static final SparseArray<Integer> TITLE_SPARSE_ARRAY = new SparseArray<Integer>() {
        {
            append(RecordDataType.POINTS, R.string.points);
            append(RecordDataType.TWO_POINT_SHOT, R.string.twoPointShot);
            append(RecordDataType.TWO_POINT_SHOT_MADE, R.string.twoPointShotMade);
            append(RecordDataType.TWO_POINT_SHOT_MISSED, R.string.twoPointShotMiss);
            append(RecordDataType.THREE_POINT_SHOT, R.string.threePointShot);
            append(RecordDataType.THREE_POINT_SHOT_MADE, R.string.threePointShotMade);
            append(RecordDataType.THREE_POINT_SHOT_MISSED, R.string.threePointShotMiss);
            append(RecordDataType.FREE_THROW_SHOT, R.string.freeThrowShot);
            append(RecordDataType.FREE_THROW_SHOT_MADE, R.string.freeThrowShotMade);
            append(RecordDataType.FREE_THROW_SHOT_MISSED, R.string.freeThrowShotMiss);
            append(RecordDataType.OFFENSIVE_REBOUND, R.string.offensiveRebound);
            append(RecordDataType.DEFENSIVE_REBOUND, R.string.defensiveRebound);
            append(RecordDataType.ASSIST, R.string.assist);
            append(RecordDataType.STEAL, R.string.steal);
            append(RecordDataType.BLOCK, R.string.block);
            append(RecordDataType.FOUL, R.string.foul);
            append(RecordDataType.OFFENSIVE_FOUL, R.string.offensive_foul);
            append(RecordDataType.DEFENSIVE_FOUL, R.string.defensive_foul);
            append(RecordDataType.TURNOVER, R.string.turnover);
        }
    };

    public static final SparseArray<String> COLUMN_NAME_SPARSE_ARRAY = new SparseArray<String>() {
        {
            append(RecordDataType.POINTS, GameDataDBContract.COLUMN_NAME_POINTS);
            append(RecordDataType.TWO_POINT_SHOT_MADE, GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE);
            append(RecordDataType.TWO_POINT_SHOT_MISSED, GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED);
            append(RecordDataType.THREE_POINT_SHOT_MADE, GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE);
            append(RecordDataType.THREE_POINT_SHOT_MISSED, GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED);
            append(RecordDataType.FREE_THROW_SHOT_MADE, GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE);
            append(RecordDataType.FREE_THROW_SHOT_MISSED, GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED);
            append(RecordDataType.OFFENSIVE_REBOUND, GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND);
            append(RecordDataType.DEFENSIVE_REBOUND, GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND);
            append(RecordDataType.ASSIST, GameDataDBContract.COLUMN_NAME_ASSIST);
            append(RecordDataType.STEAL, GameDataDBContract.COLUMN_NAME_STEAL);
            append(RecordDataType.BLOCK, GameDataDBContract.COLUMN_NAME_BLOCK);
            append(RecordDataType.FOUL, GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL);
            append(RecordDataType.TURNOVER, GameDataDBContract.COLUMN_NAME_TURNOVER);
        }
    };

    public static final int[] TYPE_CHOICE_INT = new int[]{
              -1,
              Constants.RecordDataType.TWO_POINT_SHOT_MADE,
              Constants.RecordDataType.TWO_POINT_SHOT_MISSED,
              Constants.RecordDataType.THREE_POINT_SHOT_MADE,
              Constants.RecordDataType.THREE_POINT_SHOT_MISSED,
              Constants.RecordDataType.FREE_THROW_SHOT_MADE,
              Constants.RecordDataType.FREE_THROW_SHOT_MISSED,
              Constants.RecordDataType.OFFENSIVE_REBOUND,
              Constants.RecordDataType.DEFENSIVE_REBOUND,
              Constants.RecordDataType.ASSIST,
              Constants.RecordDataType.STEAL,
              Constants.RecordDataType.BLOCK,
              Constants.RecordDataType.OFFENSIVE_FOUL,
              RecordDataType.DEFENSIVE_FOUL,
              Constants.RecordDataType.TURNOVER
    };

    public class RecordDataType {

        public static final int SHIFT_SHOT_MADE = 1;
        public static final int SHIFT_SHOT_MISSED = 2;
        public static final int SHIFT_OFFENSIVE_FOUL = 1;
        public static final int SHIFT_DEFENSIVE_FOUL = 2;

        public static final int YOUR_TEAM_TOTAL_SCORE = 0x00a1;
        public static final int OPPONENT_TEAM_TOTAL_SCORE = 0x00b1;
        public static final int YOUR_TEAM_FOUL = 0x00c1;
        public static final int OPPONENT_TEAM_FOUL = 0x00d1;
        public static final int QUARTER = 0x00e1;
        public static final int MINUS_YOUR_TEAM_TOTAL_SCORE = 0x10a1;
        public static final int MINUS_OPPONENT_TEAM_TOTAL_SCORE = 0x10b1;
        public static final int MINUS_YOUR_TEAM_TEAM_FOUL = 0x10c1;
        public static final int MINUS_OPPONENT_TEAM_TOTAL_FOUL = 0x10d1;
        public static final int MINUS_QUARTER = 0x10e1;

        public static final int POINTS = 0x0000;

        public static final int TWO_POINT_SHOT = 0x0001;
        public static final int TWO_POINT_SHOT_MADE = 0x0002;
        public static final int TWO_POINT_SHOT_MISSED = 0x0003;
        public static final int THREE_POINT_SHOT = 0x0011;
        public static final int THREE_POINT_SHOT_MADE = 0x0012;
        public static final int THREE_POINT_SHOT_MISSED = 0x0013;
        public static final int FREE_THROW_SHOT = 0x0021;
        public static final int FREE_THROW_SHOT_MADE = 0x0022;
        public static final int FREE_THROW_SHOT_MISSED = 0x0023;
        public static final int OFFENSIVE_REBOUND = 0x0031;
        public static final int DEFENSIVE_REBOUND = 0x0041;
        public static final int ASSIST = 0x0051;
        public static final int STEAL = 0x0061;
        public static final int BLOCK = 0x0071;
        public static final int FOUL = 0x0081;
        public static final int OFFENSIVE_FOUL = 0x0082;
        public static final int DEFENSIVE_FOUL = 0x0083;
        public static final int TURNOVER = 0x0091;

        public static final int JUDGEMENT_NUMBER = 0x1000; //if type > 0x1000 -> recovery type

        public static final int MINUS_TWO_POINT_MADE = 0x1002;
        public static final int MINUS_TWO_POINT_MISSED = 0x1003;
        public static final int MINUS_THREE_POINT_MADE = 0x1012;
        public static final int MINUS_THREE_POINT_MISSED = 0x1013;
        public static final int MINUS_FREE_THROW_MADE = 0x1022;
        public static final int MINUS_FREE_THROW_MISSED = 0x1023;
        public static final int MINUS_OFFENSIVE_REBOUND = 0x1031;
        public static final int MINUS_DEFENSIVE_REBOUND = 0x1041;
        public static final int MINUS_ASSIST = 0x1051;
        public static final int MINUS_STEAL = 0x1061;
        public static final int MINUS_BLOCK = 0x1071;
        public static final int MINUS_FOUL = 0x1081;
        public static final int MINUS_OFFENSIVE_FOUL = 0x1082;
        public static final int MINUS_DEFENSIVE_FOUL = 0x1083;
        public static final int MINUS_TURNOVER = 0x1091;

    }

    public class GameDataDBContract implements BaseColumns {

        public static final String _ID = "_id";
        public static final String TABLE_NAME = "game_data";
        public static final String COLUMN_NAME_GAME_ID = "game_id";
        public static final String COLUMN_NAME_QUARTER = "quarter";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
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
        public static final String COLUMN_NAME_PERSONAL_FOUL = "PF";
        public static final String COLUMN_NAME_TURNOVER = "TOV";

    }

    public class GameInfoDBContract implements BaseColumns {

        public static final String _ID = "_id";
        public static final String TABLE_NAME = "game_info";
        public static final String COLUMN_NAME_GAME_ID = "game_id";
        public static final String COLUMN_NAME_GAME_NAME = "game_name";
        public static final String COLUMN_NAME_YOUR_TEAM_ID = "your_team_id";
        public static final String COLUMN_NAME_YOUR_TEAM = "team";
        public static final String COLUMN_NAME_OPPONENT_NAME = "opponent";
        public static final String COLUMN_NAME_QUARTER_LENGTH = "quarter_length";
        public static final String COLUMN_NAME_TOTAL_QUARTER = "total_quarter";
        public static final String COLUMN_NAME_MAX_FOUL = "max_foul";
        public static final String COLUMN_NAME_TIMEOUT_FIRST_HALF = "timeout_first_half";
        public static final String COLUMN_NAME_TIMEOUT_SECOND_HALF = "timeout_second_half";
        public static final String COLUMN_NAME_YOUR_TEAM_SCORE = "your_score";
        public static final String COLUMN_NAME_OPPONENT_TEAM_SCORE = "opponent_score";
        public static final String COLUMN_NAME_GAME_DATE = "game_date";
        public static final String COLUMN_NAME_IS_GAMEOVER = "is_gameover";

    }

    public class TeamInfoDBContract implements BaseColumns {

        public static final String _ID = "_id";
        public static final String TABLE_NAME = "team_info";
        public static final String COLUMN_NAME_TEAM_NAME = "team_name";
        public static final String COLUMN_NAME_TEAM_ID = "team_id";
        public static final String COLUMN_NAME_TEAM_PLAYERS_AMOUNT = "players_amount";
        public static final String COLUMN_NAME_TEAM_HISTORY_AMOUNT = "history_amount";

    }

    public class TeamPlayersContract implements BaseColumns {

        public static final String _ID = "_id";
        public static final String TABLE_NAME = "team_player";
        public static final String COLUMN_NAME_TEAM_ID = "team_id";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_PLAYER_NAME = "player_name";
        public static final String COLUMN_NAME_PLAYER_NUMBER = "player_number";
        public static final String COLUMN_NAME_PLAY_C = "center";
        public static final String COLUMN_NAME_PLAY_PF = "power_forward";
        public static final String COLUMN_NAME_PLAY_SF = "small_forward";
        public static final String COLUMN_NAME_PLAY_SG = "shooting_guard";
        public static final String COLUMN_NAME_PLAY_PG = "point_guard";

    }

    public class FireBaseConstant {

        public static final String NODE_NAME_USERS = "users";
        public static final String NODE_NAME_USER_EMAIL = "email";
        public static final String NODE_NAME_TEAM_PLAYER = "team_player";
        public static final String NODE_NAME_TEAM_INFO = "team_info";
        public static final String NODE_NAME_GAME_DATA = "game_data";
        public static final String NODE_NAME_GAME_INFO = "game_info";
        public static final String NODE_NAME_QUARTER = "quarter";

    }

    public class ExtraNames {

        public static final String SETTING_BOOLEAN_IS_SHOW_LOGOUT = "isShowLogout";

    }
}
