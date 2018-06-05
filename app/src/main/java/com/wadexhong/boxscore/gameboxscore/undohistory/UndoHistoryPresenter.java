package com.wadexhong.boxscore.gameboxscore.undohistory;

import com.wadexhong.boxscore.gameboxscore.GameBoxScoreContract;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.objects.Player;
import com.wadexhong.boxscore.objects.Undo;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryPresenter implements UndoHistoryContract.Presenter{
    private static final String TAG = UndoHistoryPresenter.class.getSimpleName();

    private final UndoHistoryContract.View mUndoHistoryView;
    private final GameBoxScoreContract.Presenter mGameBoxScorePresenter;

    private LinkedList<Undo> mUndoList;
    private GameInfo mGameInfo;
    private UndoHistoryAdapter mAdapter;
    private int mUndoPosition;

    public UndoHistoryPresenter(UndoHistoryContract.View mUndoHistoryView, GameBoxScoreContract.Presenter gameBoxScorePresenter) {
        this.mUndoHistoryView = mUndoHistoryView;
        this.mGameBoxScorePresenter = gameBoxScorePresenter;
        mUndoHistoryView.setPresenter(this);
    }



    @Override
    public void start() {
        mUndoList = mGameBoxScorePresenter.getUndoList();
        mGameInfo = mGameBoxScorePresenter.getGameInfo();
    }

    @Override
    public void createAdapter() {
        mAdapter = new UndoHistoryAdapter(this, mUndoList, mGameInfo);
        mUndoHistoryView.setAdapter(mAdapter);
    }

    @Override
    public void undoAtPosition(int position) {
        mUndoPosition = position;
        mGameBoxScorePresenter.undoDataInDb(position);
    }

    @Override
    public void editUndoAtPosition(int position, Player player, int type) {
        mUndoPosition = position;
        mGameBoxScorePresenter.editAtPosition(position, player, type);
    }

    @Override
    public void updateUi() {
        mUndoHistoryView.updateUi();
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void notifyInsert() {
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void notifyRemove(int position) {
        mAdapter.notifyItemRemoved(position);

    }
}
