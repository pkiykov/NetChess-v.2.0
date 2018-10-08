package com.netchess.pkiykov.ui.screens.login.router

import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.Screen
import com.netchess.pkiykov.ui.screens.base.BaseRouter

class LoginRouter(dispatcher: BaseActivity) : BaseRouter(dispatcher) {

    fun openProfileScreen() {
        dispatcher.selectFragment(Screen.PROFILE)
    }

    fun openOnBoardingScreen() {
        dispatcher.selectFragment(Screen.ON_BOARDING)
    }
}