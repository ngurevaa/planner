package ru.kpfu.itis.gureva.feature.tasklist.impl.usecase

import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task
import ru.kpfu.itis.gureva.feature.tasklist.api.repository.TaskListRepository
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetTasksUseCase
import javax.inject.Inject

class GetTasksUseCaseImpl @Inject constructor(
    private val taskListRepository: TaskListRepository
) : GetTasksUseCase {
    override suspend operator fun invoke(groupId: Long): List<Task> {
        return taskListRepository.getTasks(groupId)
    }
}
