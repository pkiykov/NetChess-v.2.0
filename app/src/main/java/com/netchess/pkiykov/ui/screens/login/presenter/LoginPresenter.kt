package com.netchess.pkiykov.ui.screens.login.presenter

import android.content.Context
import android.content.Intent
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.core.interactors.FacebookInteractor
import com.netchess.pkiykov.core.interactors.FirebaseInteractor
import com.netchess.pkiykov.core.utils.TextValidator
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.screens.base.BasePresenter
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.login.ILogin
import com.netchess.pkiykov.ui.screens.login.router.LoginRouter
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                         private val validator: TextValidator,
                                         private val facebookInteractor: FacebookInteractor,
                                         @ForApplication context: Context) :
        BasePresenter<ILogin.View, LoginRouter>(context), ILogin.Presenter {
    override fun passFacebookLoginResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookInteractor.passResult(requestCode, resultCode, data)
    }

    override fun registerFacebookButton(facebookSignInButton: LoginButton) {
        doSafelyWithView {
            facebookInteractor.registerFacebookButton(facebookSignInButton, view as BaseView, object : FirebaseInteractor.FirebaseListener {

                override fun onSuccess() {
                    onSuccessLogin()
                }

                override fun onError(throwable: Throwable) {
                    facebookInteractor.signOut()
                    onFailLogin(Exception(throwable))
                }
            })
        }
    }

    override fun initRouter(activity: BaseActivity) {
        router = LoginRouter(activity)
    }

    override fun onBoardingClick() {
        router.openOnBoardingScreen()
    }

    override fun softValidation(text1: CharSequence, text2: CharSequence): Boolean = validator.softCredentialValidation(text1, text2)

    override fun onLoginClick(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccessLogin()
        }.addOnFailureListener {
            onFailLogin(it)
        }
    }

    private fun onSuccessLogin() {
        doSafelyWithView {
            dismissProgressDialog()
            showSuccessMessage(firebaseAuth.currentUser!!.displayName!!)
        }
        router.openProfileScreen()
    }

    private fun onFailLogin(exception: Exception?) {
        doSafelyWithView {
            dismissProgressDialog()
            exception?.let { handleError(it) }
        }
    }

}