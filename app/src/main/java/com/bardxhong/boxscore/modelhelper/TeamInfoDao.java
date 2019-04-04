package com.bardxhong.boxscore.modelhelper;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface TeamInfoDao {
    @Query("SELECT * from team_info WHERE team_name == :teamName")
    public List<TeamInfoEntity> queryByTeamName(String teamName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(TeamInfoEntity expense);
}
