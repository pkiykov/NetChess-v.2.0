package com.netchess.pkiykov.ui.screens.login.view

import android.support.design.widget.Snackbar
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.login.ILogin
import io.reactivex.rxkotlin.Observables

class LoginView : BaseView(), ILogin.View {

    private lateinit var loginButton: Button
    private lateinit var registrationButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var presenter: ILogin.Presenter

    override val name: String? = LoginView::class.qualifiedName

    override fun initPresenter() {
        presenter = App.presenterComponent().getLoginPresenter()
    }

    override fun bindViews() {
        loginButton = rootView.findViewById(R.id.login_button)
        registrationButton = rootView.findViewById(R.id.registration_button)
        emailEditText = rootView.findViewById(R.id.email_edit_text)
        passwordEditText = rootView.findViewById(R.id.password_edit_text)
    }

    override fun onCreateViewFragment() {
        val emailChangeObservable = RxTextView.textChangeEvents(emailEditText)
        val passwordChangeObservable = RxTextView.textChangeEvents(passwordEditText)

        val disposable = Observables.combineLatest(emailChangeObservable, passwordChangeObservable)
        { emailTextEvent, passwordTextEvent ->
            presenter.softValidation(emailTextEvent.text().toString(), passwordTextEvent.text().toString())
        }.subscribe { isEnabled -> loginButton.isEnabled = isEnabled }

        addDisposable(disposable)

        loginButton.setOnClickListener {
            presenter.onLoginClick(emailEditText.text.toString(),
                    passwordEditText.text.toString())
        }

        registrationButton.setOnClickListener {
            presenter.onBoardingClick()
        }
    }

    override fun getContentLayoutID(): Int = R.layout.fragment_login

    override fun showCredTooShortMessage() {
        Snackbar.make(rootView, getString(R.string.too_short_credentials, 3), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMessage(name: String) {
        Snackbar.make(rootView, getString(R.string.welcome_onboard, name), Snackbar.LENGTH_SHORT).show()
    }

    companion object {

        fun getInstance(): LoginView = LoginView()

    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

}