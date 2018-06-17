package com.netchess.pkiykov.ui.screens.onBoarding

import com.netchess.pkiykov.ui.screens.base.BaseView

class OnBoardingView : BaseView() {
    override fun name(): String? = OnBoardingView::class.qualifiedName

    companion object {
        @JvmStatic
        fun getInstance(): OnBoardingView = OnBoardingView()
    }
}