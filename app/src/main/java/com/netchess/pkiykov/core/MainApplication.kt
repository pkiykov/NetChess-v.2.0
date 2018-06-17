package com.netchess.pkiykov.core

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.netchess.pkiykov.core.dagger.ApplicationComponent
import com.netchess.pkiykov.core.dagger.CoreModule
import com.netchess.pkiykov.core.dagger.DaggerApplicationComponent
import javax.inject.Inject

class MainApplication : Application() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    @Inject
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder()
                .coreModule(CoreModule(this))
                .build()
        graph.inject(this)
        firebaseDatabase.setPersistenceEnabled(false)
    }

    companion object {
        @JvmStatic
        lateinit var graph: ApplicationComponent
    }

}