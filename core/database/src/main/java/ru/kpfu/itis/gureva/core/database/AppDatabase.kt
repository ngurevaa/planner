package ru.kpfu.itis.gureva.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.gureva.core.database.dao.GroupDao
import ru.kpfu.itis.gureva.core.database.dao.TaskDao
import ru.kpfu.itis.gureva.core.database.entity.GroupEntity
import ru.kpfu.itis.gureva.core.database.entity.TaskEntity

@Database(entities = [GroupEntity::class, TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
}
