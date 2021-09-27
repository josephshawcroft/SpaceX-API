package com.josephshawcroft.spacexapi.di

import com.josephshawcroft.spacexapi.network.SpaceXApiService
import com.josephshawcroft.spacexapi.repository.SpaceXRepository
import com.josephshawcroft.spacexapi.repository.SpaceXRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLaunchListRepository(spaceXApiService: SpaceXApiService): SpaceXRepository =
        SpaceXRepositoryImpl(spaceXApiService)
}