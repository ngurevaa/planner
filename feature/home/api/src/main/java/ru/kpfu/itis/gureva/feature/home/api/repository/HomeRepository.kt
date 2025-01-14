package ru.kpfu.itis.gureva.feature.home.api.repository

interface HomeRepository {
    suspend fun getAllGroups()
}
