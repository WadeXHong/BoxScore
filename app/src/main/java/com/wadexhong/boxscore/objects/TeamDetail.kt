package com.wadexhong.boxscore.objects

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wade8 on 2018/5/20.
 */
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamDetail(
    var teamName: String? = null,
    var teamId: String? = null,
    var teamPlayersAmount: Int = 0,
    var teamHistoryAmount: Int = 0
) : Parcelable
