package com.netchess.pkiykov.core.dagger

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.netchess.pkiykov.core.interactors.FirebaseInteractor
import com.netchess.pkiykov.core.utils.TextValidator
import com.netchess.pkiykov.ui.screens.PerScreen
import com.netchess.pkiykov.ui.screens.login.presenter.LoginPresenter
import com.netchess.pkiykov.ui.screens.onBoarding.presenter.OnBoardingPresenter
import com.netchess.pkiykov.ui.screens.profile.presenter.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    @PerScreen
    fun provideProfilePresenter(firebaseInteractor: FirebaseInteractor,
                                @ForApplication appContext: Context): ProfilePresenter =
            ProfilePresenter(firebaseInteractor, appContext)

    @Provides
    @PerScreen
    fun provideLoginPresenter(firebaseAuth: FirebaseAuth,
                              validator: TextValidator,
                              @ForApplication appContext: Context): LoginPresenter =
            LoginPresenter(firebaseAuth, validator, appContext)

    @Provides
    @PerScreen
    fun provideOnBoardingPresenter(firebaseAuth: FirebaseAuth,
                                   validator: TextValidator,
                                   @ForApplication appContext: Context): OnBoardingPresenter =
            OnBoardingPresenter(firebaseAuth, validator, appContext)
}