package com.netchess.pkiykov.core

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.netchess.pkiykov.core.dagger.*
import com.netchess.pkiykov.core.interactors.InteractorModule
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()
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