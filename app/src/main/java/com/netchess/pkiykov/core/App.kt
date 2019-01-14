package com.netchess.pkiykov.core

import android.support.multidex.MultiDexApplication
import com.google.firebase.database.FirebaseDatabase
import com.netchess.pkiykov.core.dagger.*
import com.netchess.pkiykov.core.interactors.InteractorModule
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject

class App : MultiDexApplication() {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...

        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(ContextModule(this))
                .firebaseModule(FirebaseModule())
                .build()
        applicationComponent.inject(this)
        firebaseDatabase.setPersistenceEnabled(false)
    }

    companion object {

        lateinit var applicationComponent: ApplicationComponent

        private var presenterComponent: PresenterComponent? = null

        fun presenterComponent(): PresenterComponent {
            if (presenterComponent == null)
                presenterComponent = applicationComponent.plusPresenterComponent(PresenterModule(), InteractorModule())

            return presenterComponent!!
        }
    }

}