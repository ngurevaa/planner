package ru.kpfu.itis.gureva.feature.tasklist.impl.data.mapper

import ru.kpfu.itis.gureva.core.database.entity.TaskEntity
import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task

fun List<TaskEntity>.mapList(): List<Task> = map {
    it.toTask()
}

fun TaskEntity.toTask(): Task = Task(
    id = id,
    name = name,
    calendar = null
)
