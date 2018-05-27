package com.wadexhong.boxscore.historyplayersdata;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.adapter.HistoryPlayersDataStatisticAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPlayersDataFragment extends Fragment implements HistoryPlayersDataContract.View{

    private static final String TAG = HistoryPlayersDataFragment.class.getSimpleName();

    private HistoryPlayersDataContract.Presenter mPresenter;

    private AdaptiveTableLayout mAdaptiveTableLayout;



    public static HistoryPlayersDataFragment newInstance(){
        return new  HistoryPlayersDataFragment();
    }

    public HistoryPlayersDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_players_data, container, false);
        mAdaptiveTableLayout = view.findViewById(R.id.fragment_history_playersdata_tablelayout);

        mPresenter.setAdapter();

        return view;
    }

    @Override
    public void setPresenter(HistoryPlayersDataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setAdapter(final HistoryPlayersDataStatisticAdapter adapter) {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int row, int column) {

            }

            @Override
            public void onRowHeaderClick(int row) {

            }

            @Override
            public void onColumnHeaderClick(int column) {

            }

            @Override
            public void onLeftTopHeaderClick() {
                final CharSequence[] charSequences = new CharSequence[]{getResources().getString(R.string.total),
                          getResources().getString(R.string.firstQuarter),
                          getResources().getString(R.string.secondQuarter),
                          getResources().getString(R.string.thirdQuarter),
                          getResources().getString(R.string.forthQuarter)};
                new AlertDialog.Builder(getContext())
                          .setTitle(R.string.quarterFilter)
                          .setItems(charSequences, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  adapter.chooseFilter(which);
//                                  mQuarterFilter = which;
//                                  refreshCursor(mGameId);
//                                  mTextView.setText(charSequences[which]);
                              }
                          }).show();
            }
        });
        mAdaptiveTableLayout.setAdapter(adapter);
    }
}
