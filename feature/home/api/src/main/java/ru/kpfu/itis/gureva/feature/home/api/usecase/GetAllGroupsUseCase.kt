package ru.kpfu.itis.gureva.feature.home.api.usecase

import ru.kpfu.itis.gureva.feature.home.api.model.Group

interface GetAllGroupsUseCase {
    suspend operator fun invoke(): List<Group>
}
