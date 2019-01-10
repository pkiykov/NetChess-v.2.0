package com.netchess.pkiykov.core.interactors

import android.content.Context
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.ui.screens.base.BaseView
import java.util.*
import javax.inject.Inject

class FacebookInteractor @Inject constructor(@ForApplication val context: Context,
                                             val firebaseInteractor: FirebaseInteractor) {

    fun signOut() {
        LoginManager.getInstance().logOut()
    }

    companion object {

        private const val EMAIL = "email"
        private const val PUBLIC_PROFILE = "public_profile"

    }

    private var callbackManager: CallbackManager? = null

    fun registerFacebookButton(facebookSignInButton: LoginButton, baseView: BaseView, listener: FirebaseInteractor.FirebaseListener) {
        callbackManager = CallbackManager.Factory.create()
        facebookSignInButton.apply {
            fragment = baseView
            setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE))
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    firebaseInteractor.firebaseAuthWithCredential(FacebookAuthProvider.getCredential(loginResult.accessToken.token), listener)
                }

                override fun onCancel() {

                }

                override fun onError(exception: FacebookException) {
                    listener.onError(exception)
                }
            })
        }
    }

    fun passResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}