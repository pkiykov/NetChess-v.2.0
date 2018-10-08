package com.netchess.pkiykov.ui.screens.onBoarding.presenter

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.Constants
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.core.models.Player
import com.netchess.pkiykov.core.utils.TextValidator
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.screens.base.BasePresenter
import com.netchess.pkiykov.ui.screens.onBoarding.IOnBoarding
import com.netchess.pkiykov.ui.screens.onBoarding.router.OnBoardingRouter
import javax.inject.Inject


class OnBoardingPresenter @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                              private val validator: TextValidator,
                                              @ForApplication context: Context) :
        BasePresenter<IOnBoarding.View, OnBoardingRouter>(context), IOnBoarding.Presenter {

    override fun initRouter(activity: BaseActivity) {
        router = OnBoardingRouter(activity)
    }

    override fun onSubmitButtonClick(name: String, email: String, password: String, year: Int, month: Int, dayOfMonth: Int) {
        doSafelyWithView { showProgressDialog() }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { _ ->
            doSafelyWithView { dismissProgressDialog() }

            val date = year.toString() + "-" +
                    String.format("%02d", month + 1) + "-" +
                    String.format("%02d", dayOfMonth)
            val playerId = firebaseAuth.currentUser!!.uid
            val player = Player(playerId, name, date)
            FirebaseDatabase.getInstance().reference.child(Constants.PLAYERS).child(playerId).setValue(player)
                    .continueWithTask {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build()
                        firebaseAuth.currentUser!!.updateProfile(profileUpdates)
                    }
                    .addOnSuccessListener {
                        doSafelyWithView {
                            dismissProgressDialog()
                            showSimpleSnackbarMessage(context.getString(R.string.welcome_onboard, name))
                            router.openProfileScreen()
                        }
                    }.addOnFailureListener { exception ->
                        doSafelyWithView {
                            dismissProgressDialog()
                            handleError(exception)
                        }
                    }

        }.addOnFailureListener {
            doSafelyWithView {
                dismissProgressDialog()
                handleError(it)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        //TODO: Add Logging (TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseAuth.currentUser

                    } else {

                    }
                }
    }

    override fun validateEmail(text: CharSequence): Boolean {
        return if (validator.validateEmail(text)) {
            true
        } else {
            doSafelyWithView { showInvalidEmailMessage(context.getString(R.string.invalid_email)) }
            false
        }
    }

    override fun validatePassword(text: CharSequence): Boolean {
        return if (validator.validatePassword(text)) {
            true
        } else {
            doSafelyWithView { showInvalidPasswordMessage(context.getString(R.string.invalid_password)) }
            false
        }
    }

    override fun validateName(text: CharSequence): Boolean {
        return if (validator.validateUsername(text)) {
            true
        } else {
            doSafelyWithView { showInvalidUsernameMessage(context.getString(R.string.invalid_username)) }
            false
        }
    }


}

