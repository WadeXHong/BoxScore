package com.wadexhong.boxscore.gameboxscore.undohistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadexhong.boxscore.R;

public class UndoHistoryFragment extends Fragment implements UndoHistoryContract.View{
    private static final String TAG = UndoHistoryFragment.class.getSimpleName();

    private UndoHistoryContract.Presenter mPresenter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;

    private String mParam1;
    private String mParam2;



//    public static UndoHistoryFragment newInstance(String param1, String param2) {
//        UndoHistoryFragment fragment = new UndoHistoryFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static UndoHistoryFragment newInstance() {
        return new UndoHistoryFragment();
    }

    public UndoHistoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_undo_history, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_undohistory_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.createAdapter();

        return view;
    }

    @Override
    public void setPresenter(UndoHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setAdapter(UndoHistoryAdapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateUi() {

    }
}
