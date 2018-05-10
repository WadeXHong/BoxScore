package com.example.wade8.boxscore.undohistory;

import com.example.wade8.boxscore.adapter.UndoHistoryAdapter;
import com.example.wade8.boxscore.gameboxscore.GameBoxScoreContract;
import com.example.wade8.boxscore.objects.Undo;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryPresenter implements UndoHistoryContract.Presenter{
    private static final String TAG = UndoHistoryPresenter.class.getSimpleName();

    private final UndoHistoryContract.View mUndoHistoryView;
    private final GameBoxScoreContract.Presenter mGameBoxScorePresenter;

    private LinkedList<Undo> mUndoList;
    private UndoHistoryAdapter mAdapter;
    private int mUndoPosition;

    public UndoHistoryPresenter(UndoHistoryContract.View mUndoHistoryView, GameBoxScoreContract.Presenter mGameBoxScorePresenter) {
        this.mUndoHistoryView = mUndoHistoryView;
        this.mGameBoxScorePresenter = mGameBoxScorePresenter;
        mUndoHistoryView.setPresenter(this);
    }



    @Override
    public void start() {
        mUndoList = mGameBoxScorePresenter.getUndoList();
    }

    @Override
    public void createAdapter() {
        mAdapter = new UndoHistoryAdapter(this,mUndoList);
        mUndoHistoryView.setAdapter(mAdapter);
    }

    @Override
    public void undoAtPosition(int position) {
        mUndoPosition = position;
        mGameBoxScorePresenter.undoDataInDb(position);
    }

    @Override
    public void updateUi() {
        mUndoHistoryView.updateUi();
        mAdapter.notifyItemRemoved(mUndoPosition);
    }
}
