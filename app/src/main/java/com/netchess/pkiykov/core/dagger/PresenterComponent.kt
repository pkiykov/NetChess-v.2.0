package com.netchess.pkiykov.core.dagger

import com.netchess.pkiykov.core.interactors.InteractorModule
import com.netchess.pkiykov.ui.screens.PerScreen
import com.netchess.pkiykov.ui.screens.login.presenter.LoginPresenter
import com.netchess.pkiykov.ui.screens.onBoarding.presenter.OnBoardingPresenter
import com.netchess.pkiykov.ui.screens.profile.presenter.ProfilePresenter
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [PresenterModule::class, InteractorModule::class])
interface PresenterComponent {

    fun getProfilePresenter(): ProfilePresenter
    fun getLoginPresenter(): LoginPresenter
    fun getOnBoardingPresenter(): OnBoardingPresenter
}