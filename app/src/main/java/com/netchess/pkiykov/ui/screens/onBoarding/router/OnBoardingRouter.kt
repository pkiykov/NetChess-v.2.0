package com.netchess.pkiykov.ui.screens.onBoarding.router

import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.Screen
import com.netchess.pkiykov.ui.screens.base.BaseRouter

class OnBoardingRouter(dispatcher: BaseActivity) : BaseRouter(dispatcher) {

    fun openProfileScreen() {
        dispatcher.selectFragment(Screen.PROFILE)
    }

}