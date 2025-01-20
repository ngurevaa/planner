package ru.kpfu.itis.gureva.feature.home.impl.data.repository

import ru.kpfu.itis.gureva.core.database.entity.GroupEntity
import ru.kpfu.itis.gureva.core.database.repository.GroupRepository
import ru.kpfu.itis.gureva.feature.home.api.model.Group
import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.impl.data.mapper.mapList
import ru.kpfu.itis.gureva.feature.home.impl.data.mapper.toGroup
import javax.inject.Inject

internal class HomeRepositoryImpl @Inject constructor(
    private val groupRepository: GroupRepository
) : HomeRepository {
    override suspend fun getAllGroups(): List<Group> {
        return groupRepository.getAllGroups().mapList()
    }

    override suspend fun saveGroup(name: String) {
        groupRepository.saveGroup(name)
    }

    override suspend fun getGroupByName(name: String): Group? {
        return groupRepository.getByName(name)?.toGroup()
    }
}
