package ru.kpfu.itis.gureva.core.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.core.database.dao.GroupDao
import ru.kpfu.itis.gureva.core.database.entity.GroupEntity
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    suspend fun getAllGroups(): List<GroupEntity> {
        return withContext(Dispatchers.IO) {
            groupDao.getAll()
        }
    }

    suspend fun saveGroup(name: String): Long {
        return withContext(Dispatchers.IO) {
            groupDao.save(GroupEntity(null, name))
        }
    }

    suspend fun getByName(name: String): GroupEntity? {
        return withContext(Dispatchers.IO) {
            groupDao.getByName(name)
        }
    }

    suspend fun getNameById(id: Long): String? {
        return withContext(Dispatchers.IO) {
            return@withContext groupDao.getById(id)?.name
        }
    }
}
