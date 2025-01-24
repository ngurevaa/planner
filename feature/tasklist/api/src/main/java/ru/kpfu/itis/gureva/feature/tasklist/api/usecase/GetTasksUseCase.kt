package ru.kpfu.itis.gureva.feature.tasklist.api.usecase

import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task

interface GetTasksUseCase {
    suspend operator fun invoke(groupId: Long): List<Task>
}
