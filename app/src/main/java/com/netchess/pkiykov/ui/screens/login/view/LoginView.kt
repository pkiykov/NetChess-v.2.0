package com.netchess.pkiykov.ui.screens.login.view

import android.content.Intent
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.ui.MainActivity
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.login.ILogin
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_login.*

class LoginView : BaseView(), ILogin.View {


    private lateinit var presenter: ILogin.Presenter

    override val name: String? = LoginView::class.qualifiedName

    override fun initPresenter() {
        presenter = App.presenterComponent().getLoginPresenter()
    }

    override fun onCreateViewFragment() {

        val emailChangeObservable = RxTextView.textChangeEvents(emailField)
        val passwordChangeObservable = RxTextView.textChangeEvents(passwordField)

        googleSignInButton.setOnClickListener { (activity as MainActivity).signInWithGoogle() }

        val disposable = Observable.combineLatest(emailChangeObservable, passwordChangeObservable,
                BiFunction { emailTextEvent: TextViewTextChangeEvent, passwordTextEvent: TextViewTextChangeEvent ->
                    presenter.softValidation(emailTextEvent.text().toString(), passwordTextEvent.text().toString())
                }).subscribe { isEnabled: Boolean -> loginButton.isEnabled = isEnabled }

        addDisposable(disposable)

        loginButton.setOnClickListener {
            presenter.onLoginClick(emailField.text.toString(),
                    passwordField.text.toString())
        }

        registrationButton.setOnClickListener {
            presenter.onBoardingClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.passFacebookLoginResult(requestCode, resultCode, data)
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
        facebookSignInButton?.let { presenter.registerFacebookButton(it) }

    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

}