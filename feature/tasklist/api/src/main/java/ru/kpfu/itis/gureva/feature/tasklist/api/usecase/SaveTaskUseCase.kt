package ru.kpfu.itis.gureva.feature.tasklist.api.usecase

interface SaveTaskUseCase {
    suspend operator fun invoke(task: ru.kpfu.itis.gureva.feature.tasklist.api.model.Task)
}
