package com.josephshawcroft.spacexapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
@InstallIn(SingletonComponent::class)
class RxModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
}