package com.wadexhong.boxscore.objects

import java.io.Serializable

/**
 * Created by wade8 on 2018/5/2.
 */
data class Player @JvmOverloads constructor(
    val number: String? = null,
    val name: String? = null,
    val playerId: String? = null,
    val position: IntArray? = null
) : Serializable {

    companion object {
        const val POSITION_C: Int = 0
        const val POSITION_PF: Int = 1
        const val POSITION_SF: Int = 2
        const val POSITION_SG: Int = 3
        const val POSITION_PG: Int = 4
    }
}
