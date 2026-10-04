package com.wadexhong.boxscore.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by wade8 on 2018/5/20.
 */
@Parcelize
data class TeamDetail(
    val teamName: String,
    val teamId: String,
    val teamPlayersAmount: Int = 0,
    val teamHistoryAmount: Int = 0
) : Parcelable
