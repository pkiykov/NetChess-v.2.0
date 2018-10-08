package com.netchess.pkiykov.ui.screens.login

import com.netchess.pkiykov.ui.screens.base.ICleanArchitecture

interface ILogin {

    interface View : ICleanArchitecture.View {

        fun showCredTooShortMessage()
        fun showSuccessMessage(name: String)
    }

    interface Presenter {
        fun onLoginClick(email: String, password: String)
        fun softValidation(text1: CharSequence, text2: CharSequence): Boolean
        fun onBoardingClick()
        fun attachView(view: View)
        fun detachView()
    }
}