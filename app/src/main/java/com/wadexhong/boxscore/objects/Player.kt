package com.wadexhong.boxscore.objects

import java.io.Serializable

/**
 * Created by wade8 on 2018/5/2.
 */
class Player(var number: String?, var name: String?) : Serializable {
    var playerId: String? = null
    var position: IntArray? = null


    constructor(number: String?, name: String?, playerId: String?) : this(number, name) {
        this.playerId = playerId
    }

    constructor(number: String?, name: String?, position: IntArray?) : this(number, name) {
        this.position = position
    }

    companion object {
        const val POSITION_C: Int = 0
        const val POSITION_PF: Int = 1
        const val POSITION_SF: Int = 2
        const val POSITION_SG: Int = 3
        const val POSITION_PG: Int = 4
    }
}
