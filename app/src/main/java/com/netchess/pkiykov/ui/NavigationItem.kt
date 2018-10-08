package com.netchess.pkiykov.ui

import android.support.annotation.IdRes
import com.netchess.pkiykov.R

enum class NavigationItem(val order: Int, @IdRes val menuItemId: Int) {

    CURRENT_GAME(0, R.id.navigation_current_game),
    NEW_GAME(1, R.id.navigation_new_game),
    PROFILE(2, R.id.navigation_profile),
    RANK_LIST(3, R.id.navigation_rank_list),
    EXIT(4, R.id.navigation_exit);

    companion object {

        fun getItemById(@IdRes menuItemId: Int): NavigationItem? =
                values().firstOrNull { menuItemId == it.menuItemId }
    }

}