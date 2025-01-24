package ru.kpfu.itis.gureva.feature.tasklist.impl.data.repository

import ru.kpfu.itis.gureva.core.database.repository.GroupRepository
import ru.kpfu.itis.gureva.core.database.repository.TaskRepository
import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task
import ru.kpfu.itis.gureva.feature.tasklist.api.repository.TaskListRepository
import ru.kpfu.itis.gureva.feature.tasklist.impl.data.mapper.mapList
import javax.inject.Inject

class TaskListRepositoryImpl @Inject constructor(
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository
) : TaskListRepository {
    override suspend fun getGroupName(id: Long): String? {
        return groupRepository.getNameById(id)
    }

    override suspend fun getTasks(id: Long): List<Task> {
        return taskRepository.getAllByGroupId(id).mapList()
    }

    override suspend fun saveTask(task: Task) {

    }
}
