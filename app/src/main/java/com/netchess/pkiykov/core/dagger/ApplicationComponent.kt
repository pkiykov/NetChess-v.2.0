package com.netchess.pkiykov.core.dagger

import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.core.interactors.InteractorModule
import com.netchess.pkiykov.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class, ContextModule::class, UtilsModule::class])
interface ApplicationComponent {

    fun plusPresenterComponent(presenterModule: PresenterModule, interactorModule: InteractorModule): PresenterComponent

    fun inject(application: App)
    fun inject(mainActivity: MainActivity)

}