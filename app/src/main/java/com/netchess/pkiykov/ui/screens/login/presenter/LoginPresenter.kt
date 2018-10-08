package com.netchess.pkiykov.ui.screens.login.presenter

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.core.utils.TextValidator
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.screens.base.BasePresenter
import com.netchess.pkiykov.ui.screens.login.ILogin
import com.netchess.pkiykov.ui.screens.login.router.LoginRouter
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                         private val validator: TextValidator,
                                         @ForApplication context: Context) :
        BasePresenter<ILogin.View, LoginRouter>(context), ILogin.Presenter {

    override fun initRouter(activity: BaseActivity) {
        router = LoginRouter(activity)
    }

    override fun onBoardingClick() {
        router.openOnBoardingScreen()
    }

    override fun softValidation(text1: CharSequence, text2: CharSequence): Boolean = validator.softCredentialValidation(text1, text2)

    override fun onLoginClick(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            doSafelyWithView {
                dismissProgressDialog()
                showSuccessMessage(firebaseAuth.currentUser!!.displayName!!)
            }
            router.openProfileScreen()
        }.addOnFailureListener {
            doSafelyWithView {
                dismissProgressDialog()
                handleError(it)
            }
        }

    }

}