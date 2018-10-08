package com.netchess.pkiykov.ui.screens.base

import com.netchess.pkiykov.ui.BaseActivity

interface ICleanArchitecture {

    interface View {
        fun handleError(throwable: Throwable)
        fun showProgressDialog()
        fun dismissProgressDialog()
        fun showSimpleSnackbarMessage(message: String)
        fun setupActivity(): BaseActivity
    }

    interface Presenter<V : View> {

        fun attachView(view: V)
        fun detachView()
    }
}