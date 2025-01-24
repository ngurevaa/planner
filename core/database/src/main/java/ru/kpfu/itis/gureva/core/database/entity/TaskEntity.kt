package ru.kpfu.itis.gureva.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(entity = GroupEntity::class, parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"), onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "group_id")
    val groupId: Int,
    val task: String
)
