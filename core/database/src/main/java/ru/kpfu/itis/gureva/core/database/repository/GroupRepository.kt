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

    suspend fun saveGroup(name: String) {
        withContext(Dispatchers.IO) {
            groupDao.save(GroupEntity(null, name))
        }
    }
}
