package ru.kpfu.itis.gureva.feature.tasklist.impl.usecase

import android.util.Log
import ru.kpfu.itis.gureva.core.utils.ResourceManager
import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task
import ru.kpfu.itis.gureva.feature.tasklist.api.repository.TaskListRepository
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.SaveTaskUseCase
import ru.kpfu.itis.gureva.feature.tasklist.impl.R
import ru.kpfu.itis.gureva.feature.tasklist.impl.exception.EmptyTaskException
import javax.inject.Inject

class SaveTaskUseCaseImpl @Inject constructor(
    private val taskListRepository: TaskListRepository,
    private val resourceManager: ResourceManager
) : SaveTaskUseCase {
    override suspend fun invoke(task: Task) {
        if ((task.name ?: "").isEmpty()) {
            throw EmptyTaskException(message = resourceManager.getString(R.string.error_empty_task))
        }
        taskListRepository.saveTask(task)
    }
}
