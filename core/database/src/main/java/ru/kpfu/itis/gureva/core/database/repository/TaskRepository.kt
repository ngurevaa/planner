package ru.kpfu.itis.gureva.core.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.core.database.dao.TaskDao
import ru.kpfu.itis.gureva.core.database.entity.TaskEntity
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun getAllByGroupId(id: Int): List<TaskEntity> {
        return withContext(Dispatchers.IO) {
            taskDao.getAll(id)
        }
    }
}
