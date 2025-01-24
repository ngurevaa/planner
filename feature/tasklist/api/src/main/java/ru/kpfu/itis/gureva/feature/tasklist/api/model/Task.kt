package ru.kpfu.itis.gureva.feature.tasklist.api.model

import java.util.Calendar

data class Task(
    val id: Long?,
    val name: String,
    val calendar: Calendar?
)
