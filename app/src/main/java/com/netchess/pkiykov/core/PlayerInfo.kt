package com.netchess.pkiykov.core

enum class PlayerInfo(val position: Int) {

    RATING(0),
    AGE(1),
    WINS(2),
    LOSSES(3),
    DRAWS(4),
    TOTAL_GAMES(5);


    companion object {

        fun getByPosition(position: Int): PlayerInfo? =
                PlayerInfo.values().firstOrNull { position == it.position }
    }

}