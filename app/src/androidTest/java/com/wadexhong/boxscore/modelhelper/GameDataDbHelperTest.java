package com.wadexhong.boxscore.modelhelper;

import android.support.test.InstrumentationRegistry;
import android.util.SparseArray;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by wade8 on 2018/6/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameDataDbHelperTest {

    private final String MOCKED_GAME_ID = "mockedGameId";
    GameInfo mockedGameInfo;
    ArrayList<Player> mockedStartingPlayerList;
    ArrayList<Player> mockedSubstitutePlayerList;


    @Before
    public void mockPlayer(){

        mockedGameInfo = new GameInfo();

        Player player1 = new Player("1", "player1","Id_1");
        Player player2 = new Player("2", "player2","Id_2");
        Player player3 = new Player("3", "player3","Id_3");
        Player player4 = new Player("4", "player4","Id_4");
        Player player5 = new Player("5", "player5","Id_5");
        Player player6 = new Player("6", "player6","Id_6");

        mockedStartingPlayerList = new ArrayList<>();
        mockedSubstitutePlayerList = new ArrayList<>();

        mockedStartingPlayerList.add(player1);
        mockedStartingPlayerList.add(player2);
        mockedStartingPlayerList.add(player3);
        mockedStartingPlayerList.add(player4);
        mockedStartingPlayerList.add(player5);

        mockedGameInfo.setStartingPlayerList(mockedStartingPlayerList);
        mockedGameInfo.setSubstitutePlayerList(mockedSubstitutePlayerList);


    }

    @Test
    public void writeInitDataIntoDataBase() throws Exception {

        GameDataDbHelper dbHelper = new GameDataDbHelper(InstrumentationRegistry.getContext());
        dbHelper.setGameInfo(mockedGameInfo);
        long value = dbHelper.writeInitDataIntoDataBase();
        assertEquals(0, value);
    }

}