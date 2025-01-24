package ru.kpfu.itis.gureva.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.gureva.core.database.entity.GroupEntity

@Dao
interface GroupDao {
    @Query("select * from groups")
    fun getAll(): List<GroupEntity>

    @Insert
    fun save(entity: GroupEntity): Long

    @Query("select * from groups where name = :name")
    fun getByName(name: String): GroupEntity?

    @Query("select * from groups where id = :id")
    fun getById(id: Long): GroupEntity?
}
