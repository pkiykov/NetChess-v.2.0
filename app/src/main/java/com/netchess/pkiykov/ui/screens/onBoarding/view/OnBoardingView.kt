package com.netchess.pkiykov.ui.screens.onBoarding.view

import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.onBoarding.IOnBoarding
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingView : BaseView(), IOnBoarding.View {

    private lateinit var presenter: IOnBoarding.Presenter

    override val name: String? = OnBoardingView::class.qualifiedName

    override fun initPresenter() {
        presenter = App.presenterComponent().getOnBoardingPresenter()
    }

    override fun onCreateViewFragment() {
        submitButton.setOnClickListener {
            presenter.onSubmitButtonClick(
                    nameField!!.text.toString(),
                    emailField!!.text.toString(),
                    passwordField!!.text.toString(),
                    birthdate.year, birthdate.month, birthdate.dayOfMonth)
        }
    }

    override fun getContentLayoutID(): Int = R.layout.fragment_onboarding

    companion object {
        fun getInstance(): OnBoardingView = OnBoardingView()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun showInvalidEmailMessage(errorMessage: String) {
        emailField!!.error = errorMessage
    }

    override fun showInvalidUsernameMessage(errorMessage: String) {
        nameField!!.error = errorMessage
    }

    override fun showInvalidPasswordMessage(errorMessage: String) {
        nameField!!.error = errorMessage
    }

}