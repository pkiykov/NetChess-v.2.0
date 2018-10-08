package com.netchess.pkiykov.ui.screens.onBoarding

import com.netchess.pkiykov.ui.screens.base.ICleanArchitecture

interface IOnBoarding {

    interface View : ICleanArchitecture.View {
        fun showInvalidEmailMessage(errorMessage: String)
        fun showInvalidUsernameMessage(errorMessage: String)
        fun showInvalidPasswordMessage(errorMessage: String)
    }

    interface Presenter {
        fun validateEmail(text: CharSequence): Boolean
        fun validatePassword(text: CharSequence): Boolean
        fun validateName(text: CharSequence): Boolean
        fun onSubmitButtonClick(name: String, email: String, password: String, year: Int, month: Int, dayOfMonth: Int)
        fun detachView()
        fun attachView(view: View)


    }
}