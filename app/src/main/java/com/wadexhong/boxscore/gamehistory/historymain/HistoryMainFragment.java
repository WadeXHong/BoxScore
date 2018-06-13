package com.wadexhong.boxscore.gamehistory.historymain;


import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wadexhong.boxscore.R;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class HistoryMainFragment extends Fragment implements HistoryMainContract.View {

    private static final String TAG = HistoryMainFragment.class.getSimpleName();

    private HistoryMainContract.Presenter mPresenter;

    private RecyclerView mGameHistoryRecyclerView;
    private HistoryMainAdapter mHistoryMainAdapter;

    private String mGameId;

    public HistoryMainFragment() {
        // Required empty public constructor
    }

    public static HistoryMainFragment newInstance() {
        return new HistoryMainFragment();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) mPresenter.setGameHistoryToolBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_main, container, false);

        mGameHistoryRecyclerView = view.findViewById(R.id.fragment_historymain_recyclerview);
        mHistoryMainAdapter = new HistoryMainAdapter(mPresenter);
        mGameHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGameHistoryRecyclerView.setAdapter(mHistoryMainAdapter);

        return view;
    }

    @Override
    public void confirmShareGameHistory(String gameId) {
        mGameId = gameId;
        HistoryMainFragmentPermissionsDispatcher.requestPermissionWithPermissionCheck(this);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(HistoryMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestPermission() {

        new AlertDialog.Builder(getContext(), R.style.OrangeDialog)
                  .setTitle("分享確認")
                  .setMessage("是否將此場比賽轉檔成.xls 並上傳至雲端？")
                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          mPresenter.createAndShareGameHistoryXls(mGameId);
                      }
                  })
                  .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  })
                  .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HistoryMainFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
    }
}
