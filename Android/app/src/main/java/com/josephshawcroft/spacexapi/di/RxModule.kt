package com.josephshawcroft.spacexapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
@InstallIn(ApplicationComponent::class)
class RxModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
}