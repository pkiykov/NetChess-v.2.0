package com.netchess.pkiykov.core.interactors

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.dagger.ForApplication
import io.reactivex.Single
import javax.inject.Inject

class GoogleInteractor @Inject constructor(@ForApplication val context: Context,
                                           val firebaseInteractor: FirebaseInteractor) {

    val googleSignInClient: GoogleSignInClient
        get() = GoogleSignIn.getClient(context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build())

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Single<AuthResult> =
            firebaseInteractor.firebaseAuthWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))

    fun proceedWithAuthData(data: Intent?): Single<AuthResult> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            Single.error(e)
        }
    }

}