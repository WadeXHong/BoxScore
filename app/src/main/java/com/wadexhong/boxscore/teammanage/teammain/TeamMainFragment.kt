package com.wadexhong.boxscore.teammanage.teammain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.R


/**
 * A simple [Fragment] subclass.
 */
class TeamMainFragment : Fragment(), TeamMainContract.View {
    private var mPresenter: TeamMainContract.Presenter? = null

    private var mCreateTeamLayout: ConstraintLayout? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_team_main, container, false)

        mRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_teammain_recyclerview)
        mCreateTeamLayout =
            view.findViewById<ConstraintLayout>(R.id.fragment_teamplayer_createplayer_layout)
        mCreateTeamLayout!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mPresenter!!.pressedCreateTeam()
            }
        })

        // Adapter will be set when showTeams is called by Presenter
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(getContext()))

        return view
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mPresenter!!.refreshToolBar()
        }
    }


    override fun setPresenter(presenter: TeamMainContract.Presenter) {
        mPresenter = presenter
    }

    override fun showTeams(teams: List<com.wadexhong.boxscore.objects.TeamInfo>) {
        mRecyclerView!!.adapter = TeamAdapter(teams, mPresenter!!)
    }

    companion object {
        private val TAG: String = TeamMainFragment::class.java.getSimpleName()

        @JvmStatic
        fun newInstance(): TeamMainFragment {
            return TeamMainFragment()
        }
    }
}
