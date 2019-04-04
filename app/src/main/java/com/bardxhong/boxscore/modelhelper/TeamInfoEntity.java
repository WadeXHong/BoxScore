package com.bardxhong.boxscore.modelhelper;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.bardxhong.boxscore.Constants;

import java.util.UUID;

@Entity(tableName = Constants.TeamInfoDBContract.TABLE_NAME)
public class TeamInfoEntity {

    @PrimaryKey
    public int _id;

    @ColumnInfo(name = Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID)
    @NonNull
    public String mTeamId;

    @ColumnInfo(name = Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME)
    public String mTeamName;

    @ColumnInfo(name = Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT)
    public int mTeamPlayerAmount;

    @ColumnInfo(name = Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT)
    public int mTeamHistoryAmount;

    public TeamInfoEntity(String teamName) {
        mTeamId = UUID.randomUUID().toString();
        mTeamName = teamName;
        mTeamPlayerAmount = 0;
        mTeamHistoryAmount = 0;
    }
}
