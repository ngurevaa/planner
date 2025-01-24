package ru.kpfu.itis.gureva.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.kpfu.itis.gureva.core.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("select * from tasks where group_id = :id")
    fun getAll(id: Long): List<TaskEntity>
}
