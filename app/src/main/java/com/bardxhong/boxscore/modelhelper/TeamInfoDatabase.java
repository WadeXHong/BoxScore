package com.bardxhong.boxscore.modelhelper;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bardxhong.boxscore.Constants;

@Database(entities = TeamInfoEntity.class, version = 1, exportSchema = false)
public abstract class TeamInfoDatabase extends RoomDatabase {

    private static TeamInfoDatabase sInstance;

    public static void init(Context context) {
        sInstance = Room.databaseBuilder(context, TeamInfoDatabase.class, Constants.TeamInfoDBContract.TABLE_NAME)
                  .addMigrations(MIGRATION_1_2)
                  .allowMainThreadQueries().build();
    }

    public static TeamInfoDatabase getIntance() {
        return sInstance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public abstract TeamInfoDao teamInfoDao();

}
