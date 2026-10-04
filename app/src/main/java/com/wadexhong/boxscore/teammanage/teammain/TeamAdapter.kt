package com.wadexhong.boxscore.teammanage.teammain

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.objects.TeamDetail
import com.wadexhong.boxscore.objects.TeamInfo
import com.wadexhong.boxscore.teammanage.teammain.TeamAdapter.DetailViewHolder
import com.wadexhong.boxscore.teammanage.teammain.TeamAdapter.TeamViewHolder

/**
 * Created by wade8 on 2018/5/20.
 */
@Deprecated("Replaced by TeamMainViewModel and TeamMainScreen Compose UI in Phase 6/7")
class TeamAdapter(
    groups: List<out ExpandableGroup<*>>,
    private val mTeamMainPresenter: TeamMainContract.Presenter
) : ExpandableRecyclerViewAdapter<TeamViewHolder, DetailViewHolder>(groups) {
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_team_teamname, parent, false)
        return TeamViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_team_teamdetail, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindChildViewHolder(
        holder: DetailViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val detail = (group as TeamInfo).getItems().get(childIndex)
        holder.setView(detail!!)
    }

    override fun onBindGroupViewHolder(
        holder: TeamViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        holder.setTitle(group)
    }

    inner class TeamViewHolder(itemView: View) : GroupViewHolder(itemView) {
        private val mDeleteImageView: ImageView
        private val mArrowImageView: ImageView
        private val mTeamNameTextView: TextView

        init {
            mDeleteImageView = itemView.findViewById<ImageView>(R.id.item_team_teamname_delete)
            mArrowImageView = itemView.findViewById<ImageView>(R.id.item_team_teamname_icon)
            mTeamNameTextView = itemView.findViewById<TextView>(R.id.item_team_teamname_teamname)
        }

        fun setTitle(group: ExpandableGroup<*>) {
            val teamName = group.getTitle()
            val teamId = (group as TeamInfo).getItems().get(0)!!.teamId

            mTeamNameTextView.setText(teamName)
            mDeleteImageView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    AlertDialog.Builder(v.getContext(), R.style.OrangeDialog)
                        .setTitle(R.string.dialog_delete_team_title)
                        .setMessage(
                            BoxScore.getStringEasy(
                                R.string.dialog_delete_team_message,
                                teamName
                            )
                        )
                        .setPositiveButton(
                            R.string.confirm,
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {
                                    mTeamMainPresenter.deleteTeam(teamId)
                                    dialog.dismiss()
                                }
                            })
                        .setNegativeButton(
                            R.string.cancel,
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {
                                    dialog.cancel()
                                }
                            }).show()
                }
            })
        }

        override fun expand() {
            animateExpand()
        }

        override fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate =
                RotateAnimation(
                    0f,
                    90f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
            rotate.setDuration(150)
            rotate.setFillAfter(true)
            mArrowImageView.setAnimation(rotate)
        }

        private fun animateCollapse() {
            val rotate =
                RotateAnimation(
                    90f,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
            rotate.setDuration(150)
            rotate.setFillAfter(true)
            mArrowImageView.setAnimation(rotate)
        }
    }

    inner class DetailViewHolder(itemView: View) : ChildViewHolder(itemView) {
        private val TAG: String = DetailViewHolder::class.java.getSimpleName()

        private val mPlayersLayout: ConstraintLayout
        private val mHistoryLayout: ConstraintLayout
        private val mPlayersAmountTextView: TextView
        private val mHistoryAmountTextView: TextView

        init {
            mPlayersLayout =
                itemView.findViewById<ConstraintLayout>(R.id.item_team_teamdetail_players_layout)
            mHistoryLayout =
                itemView.findViewById<ConstraintLayout>(R.id.item_team_teamdetail_history_layout)
            mPlayersAmountTextView =
                itemView.findViewById<TextView>(R.id.item_team_teamdetail_players_textview)
            mHistoryAmountTextView =
                itemView.findViewById<TextView>(R.id.item_team_teamdetail_history_textview)
        }

        fun setView(detail: TeamDetail) {
            val teamId = detail.teamId

            mHistoryAmountTextView.setText(
                String.format(
                    itemView.getContext().getResources().getString(
                        R.string.historyAmount
                    ), detail.teamHistoryAmount
                )
            )
            mPlayersAmountTextView.setText(
                String.format(
                    itemView.getContext().getResources().getString(
                        R.string.playersAmount
                    ), detail.teamPlayersAmount
                )
            )

            mHistoryLayout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    Log.d(TAG, "history layout pressed")
                }
            })
            mPlayersLayout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    Log.d(TAG, "players layout pressed")
                    mTeamMainPresenter.pressedTeamPlayers(teamId)
                }
            })
        }
    }
}
