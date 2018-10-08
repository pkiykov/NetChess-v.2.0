package com.netchess.pkiykov.core.dagger

import com.netchess.pkiykov.core.utils.TextValidator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideTextValidator(): TextValidator = TextValidator()
}