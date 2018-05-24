package com.example.wade8.boxscore.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade8.boxscore.Constants;
import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.historyteamdata.HistoryTeamDataContract;
import com.example.wade8.boxscore.objects.PercentFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade8 on 2018/5/23.
 */

public class HistoryTeamDataAdapter extends RecyclerView.Adapter{

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_TABLE = 1;
    private static final int VIEWTYPE_GRAPH = 2;
    private static final int DATA_TYPE_COUNT = 14;
    private static final int COLUMN_POSITION_DISPLACEMENT = 5;

    private HistoryTeamDataContract.Presenter mHistoryTeamDataPresenter;

    private String mGameId;
    private Cursor mCursorData;
    private Cursor mCursorInfo;

    private ArrayList mArrayList;

    public HistoryTeamDataAdapter(HistoryTeamDataContract.Presenter presenter){
        mHistoryTeamDataPresenter = presenter;

        refreshCursor(mGameId);
    }



    public void refreshCursor(String gameId) {
        if (gameId != null) {
            mGameId = gameId;
            mCursorData = mHistoryTeamDataPresenter.getHistoryStatistic(mGameId);
            mCursorInfo = mHistoryTeamDataPresenter.getHistoryInfo(mGameId);
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_TABLE){
            return new TableViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_table, parent, false));
        }else if (viewType == VIEWTYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_header, parent, false));
        }else {
            return new GraphViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_teamdata_chart, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mGameId != null) {
            if (position == 0) {
                ((HeaderViewHolder) holder).bind();
            }else if (position == DATA_TYPE_COUNT +1 ){
                ((GraphViewHolder) holder).bind();
            }else {
                ((TableViewHolder) holder).bind(position - 1); //Todo position change
            }
        }

    }

    @Override
    public int getItemCount() {
        return DATA_TYPE_COUNT + 2; //data types showing + header and graph
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_HEADER;
        }else if (position == DATA_TYPE_COUNT+1) {
            return VIEWTYPE_GRAPH;
        }else {
            return VIEWTYPE_TABLE;
        }
    }



    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private TextView mYourTeamScore;
        private TextView mOpponentTeamScore;
        private TextView mStatus;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mYourTeamScore = itemView.findViewById(R.id.item_history_teamdata_yourteamscore);
            mOpponentTeamScore = itemView.findViewById(R.id.item_history_teamdata_opponentteamscore);
            mStatus = itemView.findViewById(R.id.item_history_teamdata_status);
        }

        private void bind(){

            mCursorInfo.moveToFirst();
            int yourTeamScore = mCursorInfo.getInt(mCursorInfo.getColumnIndex(Constants.GameInfoDBContract.YOUR_TEAM_SCORE));
            int opponentTeamScore = mCursorInfo.getInt(mCursorInfo.getColumnIndex(Constants.GameInfoDBContract.OPPONENT_TEAM_SCORE));
            mYourTeamScore.setText(String.valueOf(yourTeamScore));
            mOpponentTeamScore.setText(String.valueOf(opponentTeamScore));
            if (yourTeamScore >= opponentTeamScore){
                mStatus.setText(R.string.win);
                mStatus.setTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorRed, null));
            }else {
                mStatus.setText(R.string.lose);
                mStatus.setTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorGreen, null));
            }
        }
    }



    public class TableViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewType;
        private TextView mTextView1;
        private TextView mTextView2;
        private TextView mTextView3;
        private TextView mTextView4;
        private TextView mTextViewTotal;





        public TableViewHolder(View itemView) {
            super(itemView);

            mTextViewType = itemView.findViewById(R.id.item_history_teamdata_table_type);
            mTextView1 = itemView.findViewById(R.id.item_history_teamdata_table_1st);
            mTextView2 = itemView.findViewById(R.id.item_history_teamdata_table_2nd);
            mTextView3 = itemView.findViewById(R.id.item_history_teamdata_table_3rd);
            mTextView4 = itemView.findViewById(R.id.item_history_teamdata_table_4th);
            mTextViewTotal = itemView.findViewById(R.id.item_history_teamdata_table_total);

        }

        private void bind(int position){

            mTextViewType.setText(Constants.COLUMN_NAME_SPARSE_ARRAY.get(Constants.COLUMN_NAME_SPARSE_ARRAY.keyAt(position)));


            int size = mCursorData.getCount();
            int first = 0, second = 0, third = 0, forth = 0, fifth = 0;

            for (int i = 0 ; i<size ; i++) {
                mCursorData.moveToPosition(i);
                switch (i) {
                    case 0:
                        first = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView1.setText(String.valueOf(first));
                    case 1:
                        second = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView2.setText(String.valueOf(second));
                    case 2:
                        third = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView3.setText(String.valueOf(third));
                    case 3:
                        forth = mCursorData.getInt(position + COLUMN_POSITION_DISPLACEMENT);
                        mTextView4.setText(String.valueOf(forth));
                    default:
                        fifth = first + second + third + forth;
                }
            }
            mTextViewTotal.setText(String.valueOf(fifth));

        }

    }


    public class GraphViewHolder extends RecyclerView.ViewHolder{

        private PieChart mThreePointPieChart;
        private PieChart mFieldGoalPieChart;

        public GraphViewHolder(View itemView) {
            super(itemView);

            mThreePointPieChart = itemView.findViewById(R.id.item_history_teamdata_3Pchart);
            mFieldGoalPieChart = itemView.findViewById(R.id.item_history_teamdata_FGchart);


        }


        private void bind(){
            int size = mCursorData.getCount();

            int[] FTA = new int[5], FTM = new int[5];
            float[] FTP = new float[5];
            int[] _2PS= new int[5], _3PS= new int[5], _1PS = new int[5];
            int[] TPA = new int[5];
            int[] TPM = new int[5];
            float[] TPP = new float[5];
            int[] FGA = new int[5];
            int[] FGM = new int[5];
            float[] FGP = new float[5];

            for (int i = 0 ; i<size ; i++) {

                mCursorData.moveToPosition(i);

                //罰球
                FTA[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED + ")"));
                FTM[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE + ")"));
                if (FTA[i] != 0) FTP[i] = (float) 100*FTM[i]/FTA[i];
                //三分
                TPA[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED + ")"));
                TPM[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE + ")"));
                if (TPA[i] != 0) TPP[i] = (float) 100*TPM[i]/TPA[i];
                //FieldGoal
                FGA[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED + ")"));
                FGM[i] = mCursorData.getInt(mCursorData.getColumnIndex("SUM(" + Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE + ")"));
                if (FGA[i] != 0) FGP[i] = (float) 100*FGM[i]/FGA[i];

                _1PS[i] = FTM[i]; _3PS[i] = 3*TPM[i]; _2PS[i] = 2*(FGM[i] - TPM[i]);

                FTA[4] += FTA[i]; FTM[4] += FTM[i];
                TPA[4] += TPA[i]; TPM[4] += TPM[i];
                FGA[4] += FGA[i]; FGM[4] += FGM[i];
                _1PS[4] += _1PS[i]; _2PS[4] += _2PS[i];_3PS[4] += _3PS[i];
            }
            // *100 顯示為 %
            if (FTA[4] != 0) FTP[4] =(float) 100*FTM[4]/FTA[4];
            if (TPA[4] != 0) TPP[4] =(float) 100*TPM[4]/TPA[4];
            if (FGA[4] != 0) FGP[4] =(float) 100*FGM[4]/FGA[4];
            mThreePointPieChart.setMaxAngle(360f*TPP[4]/100); //要先設 不然有時會顯示到舊的limit
            mFieldGoalPieChart.setMaxAngle(360f*FGP[4]/100);


            //3分圓餅
            List<PieEntry> entries3P = new ArrayList<>();
            entries3P.add(new PieEntry(TPP[4], "3P%"));

            PieDataSet set3P = new PieDataSet(entries3P,"");
            set3P.setColor(ResourcesCompat.getColor(itemView.getResources(),R.color.colorOrange, null));

            PieData data3P = new PieData(set3P);
            data3P.setValueTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorButtonInMain, null));
            data3P.setValueFormatter(new PercentFormatter());
            data3P.setDrawValues(false);//隱藏 圓上面的數字

            mThreePointPieChart.setData(data3P);

            mThreePointPieChart.setHoleColor(ResourcesCompat.getColor(itemView.getResources(), R.color.tranparent, null));
            mThreePointPieChart.setCenterText(itemView.getContext().getResources().getString(R.string.threePointInPie, String.format("%.1f", set3P.getValues().get(0).getValue()))+ " %");
            mThreePointPieChart.setCenterTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorButtonInMain, null));
            mThreePointPieChart.setDrawEntryLabels(false); //圖上面的label
            mThreePointPieChart.getLegend().setEnabled(false);//圖例隱藏
            mThreePointPieChart.getDescription().setEnabled(false); //description label 隱藏
            mThreePointPieChart.setRotationEnabled(false);
            mThreePointPieChart.spin(1500,-90,270, Easing.EasingOption.EaseInOutQuad);
            mThreePointPieChart.animateX(1500);

            //FG圓餅
            List<PieEntry> entriesFG = new ArrayList<>();
            entriesFG.add(new PieEntry(FGP[4], "FG%"));

            PieDataSet setFG = new PieDataSet(entriesFG,"");
            setFG.setColor(ResourcesCompat.getColor(itemView.getResources(),R.color.colorOrange, null));

            PieData dataFG = new PieData(setFG);
            dataFG.setValueTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorButtonInMain, null));
            dataFG.setValueFormatter(new PercentFormatter());
            dataFG.setDrawValues(false);//隱藏 圓上面的數字

            mFieldGoalPieChart.setData(dataFG);

            mFieldGoalPieChart.setHoleColor(ResourcesCompat.getColor(itemView.getResources(), R.color.tranparent, null));
            mFieldGoalPieChart.setCenterText(itemView.getContext().getResources().getString(R.string.fieldGoalInPie, String.format("%.1f", setFG.getValues().get(0).getValue()))+ " %");
            mFieldGoalPieChart.setCenterTextColor(ResourcesCompat.getColor(itemView.getResources(), R.color.colorButtonInMain, null));
            mFieldGoalPieChart.setDrawEntryLabels(false); //圖上面的label
            mFieldGoalPieChart.getLegend().setEnabled(false);//圖例隱藏
            mFieldGoalPieChart.getDescription().setEnabled(false); //description label 隱藏
            mFieldGoalPieChart.setRotationEnabled(false);
            mFieldGoalPieChart.spin(1500,-90,270, Easing.EasingOption.EaseInOutQuad);
            mFieldGoalPieChart.animateX(1500);


        }
    }

}
