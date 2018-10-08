package com.netchess.pkiykov.ui.screens.onBoarding.view

import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.onBoarding.IOnBoarding

class OnBoardingView : BaseView(), IOnBoarding.View {

    private lateinit var passwordField: EditText
    private lateinit var emailField: EditText
    private lateinit var nameField: EditText
    private lateinit var birthdate: DatePicker
    private lateinit var submitButton: Button

    private lateinit var presenter: IOnBoarding.Presenter

    override val name: String? = OnBoardingView::class.qualifiedName

    override fun initPresenter() {
        presenter = App.presenterComponent().getOnBoardingPresenter()
    }

    override fun bindViews() {
        passwordField = rootView.findViewById(R.id.password_edit_text)
        emailField = rootView.findViewById(R.id.email_edit_text)
        nameField = rootView.findViewById(R.id.name_edit_text)
        birthdate = rootView.findViewById(R.id.birthdate_picker)
        submitButton = rootView.findViewById(R.id.submit_button)
    }

    override fun onCreateViewFragment() {
        submitButton.setOnClickListener {
            presenter.onSubmitButtonClick(
                    nameField.text.toString(),
                    emailField.text.toString(),
                    passwordField.text.toString(),
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
        emailField.error = errorMessage
    }

    override fun showInvalidUsernameMessage(errorMessage: String) {
        nameField.error = errorMessage
    }

    override fun showInvalidPasswordMessage(errorMessage: String) {
        nameField.error = errorMessage
    }

}