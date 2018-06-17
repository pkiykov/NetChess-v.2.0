package com.netchess.pkiykov.ui.screens.profile.view

import com.netchess.pkiykov.ui.screens.base.BaseView

class ProfileView : BaseView() {
    override fun name(): String? = ProfileView::class.qualifiedName

    companion object {
        @JvmStatic
        fun getInstance(): ProfileView = ProfileView()
    }

}