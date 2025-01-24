package ru.kpfu.itis.gureva.feature.tasklist.api.repository

import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task

interface TaskListRepository {
    suspend fun getGroupName(id: Long): String?
    suspend fun getTasks(id: Long): List<Task>
    suspend fun saveTask(task: Task)
}
