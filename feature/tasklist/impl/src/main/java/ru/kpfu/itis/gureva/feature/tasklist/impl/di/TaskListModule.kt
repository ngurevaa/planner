package ru.kpfu.itis.gureva.feature.tasklist.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.gureva.feature.tasklist.api.repository.TaskListRepository
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetGroupNameUseCase
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetTasksUseCase
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.SaveTaskUseCase
import ru.kpfu.itis.gureva.feature.tasklist.impl.data.repository.TaskListRepositoryImpl
import ru.kpfu.itis.gureva.feature.tasklist.impl.usecase.GetGroupNameUseCaseImpl
import ru.kpfu.itis.gureva.feature.tasklist.impl.usecase.GetTasksUseCaseImpl
import ru.kpfu.itis.gureva.feature.tasklist.impl.usecase.SaveTaskUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskListModule {
    @Binds
    internal abstract fun bindTaskListRepository(taskListRepositoryImpl: TaskListRepositoryImpl): TaskListRepository

    @Binds
    internal abstract fun bindGetGroupNameUseCase(getGroupNameUseCaseImpl: GetGroupNameUseCaseImpl): GetGroupNameUseCase

    @Binds
    internal abstract fun bindGetTasksUseCase(getTasksUseCaseImpl: GetTasksUseCaseImpl): GetTasksUseCase

    @Binds
    internal abstract fun bindSaveTaskUseCase(saveTaskUseCaseImpl: SaveTaskUseCaseImpl): SaveTaskUseCase
}
