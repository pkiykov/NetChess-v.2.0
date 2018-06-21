package com.netchess.pkiykov.ui.screens.onBoarding

import com.netchess.pkiykov.ui.screens.base.BaseView

class OnBoardingView : BaseView() {

    override val name: String? = OnBoardingView::class.qualifiedName

    override fun initPresenter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentLayoutID(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    companion object {
        @JvmStatic
        fun getInstance(): OnBoardingView = OnBoardingView()
    }
}