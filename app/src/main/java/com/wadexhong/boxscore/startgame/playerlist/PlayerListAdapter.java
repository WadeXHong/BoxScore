package com.wadexhong.boxscore.startgame.playerlist;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.objects.Player;

import java.util.ArrayList;

/**
 * Created by wade8 on 2018/5/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter {

    private final static String TAG = PlayerListAdapter.class.getSimpleName();

    private final static int VIEW_TYPE_TITLE = 0;
    private final static int VIEW_TYPE_PLAYER = 1;
    private final static int VIEW_TYPE_UNREGISTERED_PLAYER = 2;
    private final static int MAX_STARTING_PLAYERS = 5;
    private final static int MAX_PLAYERS = 15;
    private final static int MIN_SUBSTITUTEPLAYERS = 1;

    private ArrayList<Player> mStartingPlayerList;
    private ArrayList<Player> mSubstitutePlayerList;
    private ArrayList<Player> mUnregisteredPlayerList;

    private boolean isOnClickProcessing = false;

    public PlayerListAdapter() {
        mockDataInit();
    }

    private void mockDataInit() {
        mStartingPlayerList = new ArrayList<>();
        mSubstitutePlayerList = new ArrayList<>();
        mUnregisteredPlayerList = new ArrayList<>();
        int x = 0;

        mUnregisteredPlayerList.add(new Player("10", "Aatrox.Simon"));
        mUnregisteredPlayerList.add(new Player("99", "Steven"));
        mUnregisteredPlayerList.add(new Player("23", "Kevin"));
        mUnregisteredPlayerList.add(new Player("94", "Luke"));
        mUnregisteredPlayerList.add(new Player("88", "Aaron"));
        mUnregisteredPlayerList.add(new Player("0", "BigRoot"));
        mUnregisteredPlayerList.add(new Player("3", "張憲騰"));
        mUnregisteredPlayerList.add(new Player("64", "天安門"));
        mUnregisteredPlayerList.add(new Player("77", "四十九"));
        mUnregisteredPlayerList.add(new Player("30", "Curry"));
        mUnregisteredPlayerList.add(new Player("24", "Kobe"));
        mUnregisteredPlayerList.add(new Player("66", "Frank"));
        mUnregisteredPlayerList.add(new Player("35", "Easy"));
        mUnregisteredPlayerList.add(new Player("87", "Wade"));
        mUnregisteredPlayerList.add(new Player("1", "穿新衣"));
        mUnregisteredPlayerList.add(new Player("2", "肚子餓"));
        mUnregisteredPlayerList.add(new Player("4", "看電視"));
        mUnregisteredPlayerList.add(new Player("5", "去跳舞"));

    }

    public void setPlayerLists(ArrayList<Player> players) {
        mStartingPlayerList = new ArrayList<>();
        mSubstitutePlayerList = new ArrayList<>();
        mUnregisteredPlayerList = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_title, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == VIEW_TYPE_UNREGISTERED_PLAYER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_unregisteredplayers, parent, false);
            return new UnregisteredPlayersViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist_players, parent, false);
            return new PlayersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).bindTitle(position);
        } else if (holder instanceof PlayersViewHolder) {
            ((PlayersViewHolder) holder).bindPlayers(position);
        } else if (holder instanceof UnregisteredPlayersViewHolder) {
            ((UnregisteredPlayersViewHolder) holder).bindUnregiseteredPlayers(position);
        } else {
            Log.w(TAG, "onBindViewHolder position error !");
        }

    }

    @Override
    public int getItemCount() {
        return 3 + getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList);
    }

    @Override
    public int getItemViewType(int position) {

        int sizeStartingPlayers = getArrayListSize(mStartingPlayerList);
        int sizeSubstitutePlayers = getArrayListSize(mSubstitutePlayerList);

        if (position == 0 | position == sizeStartingPlayers + 1 | position == sizeStartingPlayers + sizeSubstitutePlayers + 2) {
            return VIEW_TYPE_TITLE;
        } else if (position > sizeStartingPlayers + sizeSubstitutePlayers + 2) {
            return VIEW_TYPE_UNREGISTERED_PLAYER;
        } else {
            return VIEW_TYPE_PLAYER;
        }
    }

    private int getArrayListSize(ArrayList<Player> arrayList) {
        int returnValue = 0;
        if (arrayList != null) {
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

    public class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.item_playerlist_title_textview);
        }

        private void bindTitle(int position) {

            if (position == 0) {
                bindStartingPlayersTitle();
            } else if (position > 0 && position < getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + 2) {
                bindSubstitutePlayersTitle();
            } else {
                bindUnregisteredPlayersTitle();
            }
        }

        private void bindUnregisteredPlayersTitle() {
            mTitleTextView.setText(R.string.unregisteredPlayers);
            mTitleTextView.setTextColor(BoxScore.getColorEasy(R.color.colorTextInMainButton));
        }

        private void bindSubstitutePlayersTitle() {
            String title = BoxScore.getStringEasy(R.string.substitutePlayers) + BoxScore.getStringEasy(R.string.substitute_players_count, mSubstitutePlayerList.size());
            mTitleTextView.setText(title);
            if (mSubstitutePlayerList.size() > 0) {
                mTitleTextView.setTextColor(BoxScore.getColorEasy(R.color.colorGreen));
            } else {
                mTitleTextView.setTextColor(BoxScore.getColorEasy(R.color.colorAccent));
            }
        }

        private void bindStartingPlayersTitle() {
            String title = BoxScore.getStringEasy(R.string.startingPlayers) + BoxScore.getStringEasy(R.string.starting_players_count, mStartingPlayerList.size());
            mTitleTextView.setText(title);
            if (mStartingPlayerList.size() == 5) {
                mTitleTextView.setTextColor(BoxScore.getColorEasy(R.color.colorGreen));
            } else {
                mTitleTextView.setTextColor(BoxScore.getColorEasy(R.color.colorAccent));
            }
        }
    }

    public class PlayersViewHolder extends RecyclerView.ViewHolder {

        private TextView mPlayerNumberTextView;
        private TextView mPlayerNameTextView;
        private ImageView mRemoveImageView;
        private Button mStartingButton;

        public PlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerNameTextView = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumberTextView = itemView.findViewById(R.id.item_playerlist_playernumber);
            mRemoveImageView = itemView.findViewById(R.id.item_playerlist_remove);
            mStartingButton = itemView.findViewById(R.id.item_playerlist_star);

            mStartingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BoxScore.isOnClickAllowedAndSetTimer()) {

                        if (getLayoutPosition() > getArrayListSize(mStartingPlayerList) + 1) {

                            if (getArrayListSize(mStartingPlayerList) < MAX_STARTING_PLAYERS) {

                                int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;

                                mStartingPlayerList.add(mSubstitutePlayerList.get(position));
                                mSubstitutePlayerList.remove(position);
                                notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);

                            } else {
                                Toast.makeText(v.getContext(), R.string.startingPlayerLimitToast, Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            int position = getLayoutPosition() - 1;

                            mSubstitutePlayerList.add(0, mStartingPlayerList.get(position));
                            mStartingPlayerList.remove(position);
                            notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);
                        }
                    }
                }
            });
            mRemoveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BoxScore.isOnClickAllowedAndSetTimer()) {

                        if (getLayoutPosition() > getArrayListSize(mStartingPlayerList) + 1) {

                            int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - 2;

                            mUnregisteredPlayerList.add(0, mSubstitutePlayerList.get(position));
                            mSubstitutePlayerList.remove(position);
                            notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);

                        } else {

                            int position = getLayoutPosition() - 1;

                            mUnregisteredPlayerList.add(0, mStartingPlayerList.get(position));
                            mStartingPlayerList.remove(position);
                            notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);
                        }
                    }
                }
            });

        }

        private void bindPlayers(int position) {

            int positionInStartingArray = position - 1;
            int positionInSubstituteArray = position - getArrayListSize(mStartingPlayerList) - 2;

            // 替補球員
            if (position > getArrayListSize(mStartingPlayerList) + 1) {

                if (getArrayListSize(mSubstitutePlayerList) != 0) {

                    mPlayerNumberTextView.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getNumber());
                    mPlayerNameTextView.setText(mSubstitutePlayerList.get(positionInSubstituteArray).getName());
                    mStartingButton.setText("先發");

                } else {
                    Log.w(TAG, "mSubstitutePlayerList is null or empty !");
                }
            }
            //先發球員
            else {

                if (getArrayListSize(mStartingPlayerList) != 0) {

                    mPlayerNameTextView.setText(mStartingPlayerList.get(positionInStartingArray).getName());
                    mPlayerNumberTextView.setText(mStartingPlayerList.get(positionInStartingArray).getNumber());
                    mStartingButton.setText("替補");

                } else {
                    Log.w(TAG, "mStartingPlayerList is null or empty !");
                }
            }
        }
    }

    public class UnregisteredPlayersViewHolder extends RecyclerView.ViewHolder {

        private TextView mPlayerNumber;
        private TextView mPlayerName;
        private Button mAdd;

        public UnregisteredPlayersViewHolder(View itemView) {
            super(itemView);

            mPlayerName = itemView.findViewById(R.id.item_playerlist_playername);
            mPlayerNumber = itemView.findViewById(R.id.item_playerlist_playernumber);
            mAdd = itemView.findViewById(R.id.item_playerlist_add);

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BoxScore.isOnClickAllowedAndSetTimer()) {

                        if (getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mStartingPlayerList) < MAX_PLAYERS) {

                            int position = getLayoutPosition() - getArrayListSize(mStartingPlayerList) - getArrayListSize(mSubstitutePlayerList) - 3;

                            if (getArrayListSize(mStartingPlayerList) < MAX_STARTING_PLAYERS) {
                                mStartingPlayerList.add(mUnregisteredPlayerList.get(position));
                                mUnregisteredPlayerList.remove(position);
                                notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);
                            } else {
                                mSubstitutePlayerList.add(mUnregisteredPlayerList.get(position));
                                mUnregisteredPlayerList.remove(position);
                                notifyItemRangeChanged(0, getArrayListSize(mStartingPlayerList) + getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mUnregisteredPlayerList) + 3);
                            }

                        } else {
                            Toast.makeText(v.getContext(), R.string.playerLimitToast, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        private void bindUnregiseteredPlayers(int position) {

            int positionInUnregiseteredArray = position - getArrayListSize(mStartingPlayerList) - getArrayListSize(mSubstitutePlayerList) - 3;

            if (getArrayListSize(mUnregisteredPlayerList) != 0) {

                mPlayerName.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getName());
                mPlayerNumber.setText(mUnregisteredPlayerList.get(positionInUnregiseteredArray).getNumber());

                if (getArrayListSize(mSubstitutePlayerList) + getArrayListSize(mStartingPlayerList) < MAX_PLAYERS) {
                    mAdd.setEnabled(true);
                } else {
                    mAdd.setEnabled(false);
                }
                if (getArrayListSize(mStartingPlayerList) < MAX_STARTING_PLAYERS) {
                    mAdd.setText("先發");
                } else {
                    mAdd.setText("替補");
                }

            } else {
                Log.w(TAG, "mSubstitutePlayerList is null or empty !");
            }
        }
    }

}
