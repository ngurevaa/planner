package ru.kpfu.itis.gureva.feature.home.api.repository

import ru.kpfu.itis.gureva.feature.home.api.model.Group

interface HomeRepository {
    suspend fun getAllGroups(): List<Group>

    suspend fun saveGroup(name: String)

    suspend fun getGroupByName(name: String): Group?
}
