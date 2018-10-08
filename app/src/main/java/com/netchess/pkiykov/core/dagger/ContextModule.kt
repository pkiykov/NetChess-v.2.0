package com.netchess.pkiykov.core.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val appContext: Context) {

    /**
     * Allow the application context to be injected but require that it be annotated with [ ][ForApplication] to explicitly differentiate it from an setupActivity context.
     */
    @Provides
    @Singleton
    @ForApplication
    fun getAppContext(): Context = appContext

}