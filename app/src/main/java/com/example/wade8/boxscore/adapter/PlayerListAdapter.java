package com.example.wade8.boxscore.adapter;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade8.boxscore.R;
import com.example.wade8.boxscore.objects.Player;
import com.example.wade8.boxscore.objects.TransparentAlertDialog;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter{

    private final static String TAG = PlayerListAdapter.class.getSimpleName();

    private final static int TITLE_VIEW = 0;
    private final static int PLAYER_VIEW = 1;
    private final static int UNREGISTERED_PLAYER_VIEW = 2;
    private final static int MAX_STARTING_PLAYERS = 5;
    private final static int MAX_PLAYERS = 15;
    private final static int MIN_SUBSTITUTEPLAYERS = 1;


    private Handler mHandler;

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;
    private ArrayList<Player> mUnregisteredPlayerList;
    private final int[] mTitle = {R.string.startingPlayers,R.string.substitutePlayers,R.string.unregisteredPlayers};

    public PlayerListAdapter(){
        mHandler = new Handler();
        fakeDataInit();
    }

    private void fakeDataInit() { //TODO
        mStartingPlayerList = new ArrayList<>();
        mSubstitutePlayerList = new ArrayList<>();
        mUnregisteredPlayerList = new ArrayList<>();
        int x = 0;
//        for (int i = 0; i<20;i++){
//            mUnregisteredPlayerList.add(new Player(i+"",x+""));
//            x++;
//        }

        mUnregisteredPlayerList.add(new Player("99","Steven"));
        mUnregisteredPlayerList.add(new Player("23","Kevin"));
        mUnregisteredPlayerList.add(new Player("94","Luke"));
        mUnregisteredPlayerList.add(new Player("88","Aaron"));
        mUnregisteredPlayerList.add(new Player("00","BigRoot"));
        mUnregisteredPlayerList.add(new Player("03","張憲騰"));
        mUnregisteredPlayerList.add(new Player("64","天安門"));
        mUnregisteredPlayerList.add(new Player("77","四十九"));
        mUnregisteredPlayerList.add(new Player("30","Curry"));
        mUnregisteredPlayerList.add(new Player("24","Kobe"));
        mUnregisteredPlayerList.add(new Player("66","Frank"));
        mUnregisteredPlayerList.add(new Player("35","Easy"));
        mUnregisteredPlayerList.add(new Player("87","Wade"));
        mUnregisteredPlayerList.add(new Player("01","穿新衣"));
        mUnregisteredPlayerList.add(new Player("02","肚子餓"));
        mUnregisteredPlayerList.add(new Player("04","看電視"));
        mUnregisteredPlayerList.add(new Player("05","去跳舞"));

    }


    public class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.item_playerlist_title_textview);
        }

        private void bindTitle(int position){
            if (position == 0){
                String title = itemView.getContext().getResources().getString(mTitle[0]) + "　需求: 5人；目前: "+ mStartingPlayerList.size()+"人";
                mTitleTextView.setText(title);
                if (mStartingPlayerList.size() == 5){
                    mTitleTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                }else {
                    mTitleTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                }
            }else if (position>0 && position<getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+2){
                String title = itemView.getContext().getResources().getString(mTitle[1]) + "　最少: 1人；目前: "+ mSubstitutePlayerList.size()+"人";
                mTitleTextView.setText(title);
                if (mSubstitutePlayerList.size()>0){
                    mTitleTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                }else {
                    mTitleTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                }
            }else {
                mTitleTextView.setText(mTitle[2]);
                mTitleTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.colorTextInMainButton));
            }
        }
    }

    public class PlayersViewHolder extends  RecyclerView.ViewHolder{

        private TextView mPlayerNumber;
        private TextView mPlayerName;
        private ImageView mRemove;
        private ImageView mStarting;

        public PlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playerlist_playernumber);
            mRemove = itemView.findViewById(R.id.item_playerlist_remove);
            mStarting = itemView.findViewById(R.id.item_playerlist_star);
            mStarting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() > getArrayListSize(mStartingPlayerList)+1) {
                        if (getArrayListSize(mStartingPlayerList) < MAX_STARTING_PLAYERS) {
                            int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;
                            mStartingPlayerList.add(mSubstitutePlayerList.get(position));
                            mSubstitutePlayerList.remove(position);
//                            notifyItemInserted(getArrayListSize(mStartingPlayerList)+1);
                            clickBlockingThread(PlayersViewHolder.this.itemView.getContext());
                            notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList)+3);

//                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(v.getContext(),R.string.startingPlayerLimitToast,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        int position = getLayoutPosition() - 1;
                        mSubstitutePlayerList.add(0,mStartingPlayerList.get(position));
                        mStartingPlayerList.remove(position);
//                        notifyItemRemoved(getLayoutPosition());
                        clickBlockingThread(PlayersViewHolder.this.itemView.getContext());
                        notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList)+3);
                    }
                }
            });
            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldposition = getOldPosition();
                    if (getLayoutPosition() > getArrayListSize(mStartingPlayerList)+1) {
                        int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;
                        mUnregisteredPlayerList.add(0, mSubstitutePlayerList.get(position));
                        mSubstitutePlayerList.remove(position);
//                        notifyItemRemoved(getLayoutPosition());
                        clickBlockingThread(PlayersViewHolder.this.itemView.getContext());
                        notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList)+3);
                    }else {
                        int position = getLayoutPosition() - 1;
                        mUnregisteredPlayerList.add(0,mStartingPlayerList.get(position));
                        mStartingPlayerList.remove(position);
//                        notifyItemRemoved(getLayoutPosition());
                        clickBlockingThread(PlayersViewHolder.this.itemView.getContext());
                        notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList)+3);
                    }
                }
            });

        }
        private void enableClick(boolean isEnable){
            mRemove.setEnabled(isEnable);
            mStarting.setEnabled(isEnable);
        }

        private void bindPlayers(int position){
            int positionInStartingArray = position - 1;
            int positionInSubstituteArray = position - getArrayListSize(mStartingPlayerList)-2;

            // 替補球員
            if (position > getArrayListSize(mStartingPlayerList)+1){
                if (getArrayListSize(mSubstitutePlayerList) != 0) {
                    mPlayerNumber.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getNumber());
                    mPlayerName.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getName());
                    mStarting.setImageResource(R.drawable.ic_star_gray_24dp);
                }else {
                    Log.w(TAG,"mSubstitutePlayerList is null or empty !");
                }
            }
             //先發球員
              else {
                if (getArrayListSize(mStartingPlayerList) != 0) {
                    mPlayerName.setText(mStartingPlayerList.get(positionInStartingArray).getName());
                    mPlayerNumber.setText(mStartingPlayerList.get(positionInStartingArray).getNumber());
                    mStarting.setImageResource(R.drawable.ic_star_yellow_24dp);
                }else {
                    Log.w(TAG,"mStartingPlayerList is null or empty !");
                }
            }
        }
    }

    private void clickBlockingThread(Context context) {
        TransparentAlertDialog.getInstance().show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TransparentAlertDialog.getInstance().dismiss();
                    }
                });
            }
        }).start();
    }

    public class UnregisteredPlayersViewHolder extends RecyclerView.ViewHolder{

        private TextView mPlayerNumber;
        private TextView mPlayerName;
        private ImageView mAdd;

        public UnregisteredPlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playerlist_playernumber);
            mAdd = itemView.findViewById(R.id.item_playerlist_add);
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mStartingPlayerList) < MAX_PLAYERS) {
                        int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - getArrayListSize(mSubstitutePlayerList) - 3;
                        mSubstitutePlayerList.add(mUnregisteredPlayerList.get(position));
                        mUnregisteredPlayerList.remove(position);
                        clickBlockingThread(UnregisteredPlayersViewHolder.this.itemView.getContext());
                        notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList)+3);
                    }else{
                        Toast.makeText(v.getContext(),R.string.playerLimitToast,Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        private void enableClick(boolean isEnable){
            mAdd.setEnabled(isEnable);
        }
        private void bindUnregiseteredPlayers(int position){
            int positionInUnregiseteredArray = position -getArrayListSize(mStartingPlayerList)-getArrayListSize(mSubstitutePlayerList) - 3;

            if (getArrayListSize(mUnregisteredPlayerList) != 0) {
                mPlayerName.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getName());
                mPlayerNumber.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getNumber());
            }else {
                Log.w(TAG,"mSubstitutePlayerList is null or empty !");
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TITLE_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_title, parent, false);
            return new TitleViewHolder(view);
        }else if (viewType == UNREGISTERED_PLAYER_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_unregisteredplayers, parent, false);
            return new UnregisteredPlayersViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_players,parent,false);
            return new PlayersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).bindTitle(position);
        }else if (holder instanceof PlayersViewHolder){
            ((PlayersViewHolder) holder).bindPlayers(position);
        }else if (holder instanceof UnregisteredPlayersViewHolder){
            ((UnregisteredPlayersViewHolder) holder).bindUnregiseteredPlayers(position);
        }else {
            Log.w(TAG,"onBindViewHolder position error !");
        }

    }

    @Override
    public int getItemCount() {
        return 3+getArrayListSize(mStartingPlayerList)+getArrayListSize(mSubstitutePlayerList)+getArrayListSize(mUnregisteredPlayerList);
    }


    @Override
    public int getItemViewType(int position) {
        int sizeStartingPlayers = getArrayListSize(mStartingPlayerList);
        int sizeSubstitutePlayers = getArrayListSize(mSubstitutePlayerList);
        int returnValue = 1;
        if (position == 0 | position == sizeStartingPlayers+1 | position == sizeStartingPlayers+sizeSubstitutePlayers+2){
            returnValue = 0;
        }else if (position >sizeStartingPlayers+sizeSubstitutePlayers+2){
            returnValue = 2;
        }

        return returnValue;
    }


    private int getArrayListSize(ArrayList<Player> arrayList){
        int returnValue = 0;
        if (arrayList != null){
            returnValue = arrayList.size();
        }
        return returnValue;
    }

    public ArrayList<Player> getStartingPlayerList() {
        return mStartingPlayerList;
    }

    public ArrayList<Player> getSubstitutePlayerList() {
        return mSubstitutePlayerList;
    }

    public ArrayList<Player> getUnregisteredPlayerList() {
        return mUnregisteredPlayerList;
    }

}
