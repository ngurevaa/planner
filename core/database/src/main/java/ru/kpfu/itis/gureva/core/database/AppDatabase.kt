package ru.kpfu.itis.gureva.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.gureva.core.database.dao.GroupDao
import ru.kpfu.itis.gureva.core.database.entity.GroupEntity

@Database(entities = [GroupEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
}
