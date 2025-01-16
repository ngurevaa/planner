package ru.kpfu.itis.gureva.feature.home.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.api.usecase.GetAllGroupsUseCase
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import ru.kpfu.itis.gureva.feature.home.impl.data.repository.HomeRepositoryImpl
import ru.kpfu.itis.gureva.feature.home.impl.usecase.GetAllGroupsUseCaseImpl
import ru.kpfu.itis.gureva.feature.home.impl.usecase.SaveGroupUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    internal abstract fun bindGetAllGroupsUseCase(getAllGroupsUseCaseImpl: GetAllGroupsUseCaseImpl): GetAllGroupsUseCase

    @Binds
    internal abstract fun bindSaveGroupUseCase(saveGroupUseCaseImpl: SaveGroupUseCaseImpl): SaveGroupUseCase
}
