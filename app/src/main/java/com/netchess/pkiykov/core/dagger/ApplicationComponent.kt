package com.netchess.pkiykov.core.dagger

import com.netchess.pkiykov.core.MainApplication
import com.netchess.pkiykov.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(CoreModule::class)])
interface ApplicationComponent {

    fun inject(application: MainApplication)
    fun inject(mainActivity: MainActivity)

}