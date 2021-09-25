package com.josephshawcroft.spacexapi.di

import com.josephshawcroft.spacexapi.network.ApiClient
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import com.josephshawcroft.spacexapi.repository.LaunchListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLaunchListRepository(apiClient: ApiClient): LaunchListRepository =
        LaunchListRepositoryImpl(apiClient)
}