package com.example.wade8.boxscore.undohistory;

import com.example.wade8.boxscore.adapter.UndoHistoryAdapter;
import com.example.wade8.boxscore.objects.Undo;

import java.util.LinkedList;

/**
 * Created by wade8 on 2018/5/10.
 */

public class UndoHistoryPresenter implements UndoHistoryContract.Presenter{
    private static final String TAG = UndoHistoryPresenter.class.getSimpleName();

    private final UndoHistoryContract.View mUndoHistoryView;

    private LinkedList<Undo> mUndoList;

    private UndoHistoryAdapter mAdapter;

    public UndoHistoryPresenter(UndoHistoryContract.View mUndoHistoryView, LinkedList<Undo> mUndoList) {
        this.mUndoHistoryView = mUndoHistoryView;
        this.mUndoList = mUndoList;
        mUndoHistoryView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void createAdapter() {
        mAdapter = new UndoHistoryAdapter(this,mUndoList);
        mUndoHistoryView.setAdapter(mAdapter);
    }
}
