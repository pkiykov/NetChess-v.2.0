package com.netchess.pkiykov.ui.screens.base

import com.netchess.pkiykov.ui.BaseActivity

typealias D = BaseActivity

abstract class BaseRouter(protected var dispatcher: D) {

    fun openPreviousScreen() {
        dispatcher.onBackPressed()
    }
}