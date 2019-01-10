package com.netchess.pkiykov.core.interactors

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.netchess.pkiykov.ui.screens.PerScreen
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    @PerScreen
    fun provideFirebaseInteractor(firebaseAuth: FirebaseAuth,
                                  firebaseReference: DatabaseReference,
                                  storageReference: StorageReference): FirebaseInteractor =
            FirebaseInteractor(firebaseAuth, firebaseReference, storageReference)

    @Provides
    @PerScreen
    fun provideFacebookInteractor(context: Context,
                                  firebaseInteractor: FirebaseInteractor): FacebookInteractor =
            FacebookInteractor(context, firebaseInteractor)

    @Provides
    @PerScreen
    fun provideGoogleInteractor(context: Context,
                                firebaseInteractor: FirebaseInteractor): GoogleInteractor =
            GoogleInteractor(context, firebaseInteractor)
}