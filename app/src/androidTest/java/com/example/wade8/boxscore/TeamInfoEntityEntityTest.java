package com.example.wade8.boxscore;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bardxhong.boxscore.modelhelper.TeamInfoDao;
import com.bardxhong.boxscore.modelhelper.TeamInfoDatabase;
import com.bardxhong.boxscore.modelhelper.TeamInfoEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)

public class TeamInfoEntityEntityTest {

    private TeamInfoDao mTeamInfoDao;
    private TeamInfoDatabase mTeamInfoDatabase;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        TeamInfoDatabase.init(context);
        mTeamInfoDao = TeamInfoDatabase.getIntance().teamInfoDao();
    }

    @Test
    public void insertDb() {
        mTeamInfoDao.insert(new TeamInfoEntity("test_team"));

        Assert.assertEquals(mTeamInfoDao.queryByTeamName("test_team").size(), 1);
    }

    @After
    public void closeDb() throws IOException {
        TeamInfoDatabase.getIntance().close();
    }
}
