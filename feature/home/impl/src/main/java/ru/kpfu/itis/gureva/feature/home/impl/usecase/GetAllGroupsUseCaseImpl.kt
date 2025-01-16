package ru.kpfu.itis.gureva.feature.home.impl.usecase

import ru.kpfu.itis.gureva.feature.home.api.model.Group
import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.api.usecase.GetAllGroupsUseCase
import javax.inject.Inject

internal class GetAllGroupsUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository
) : GetAllGroupsUseCase {
    override suspend fun invoke(): List<Group> {
        return homeRepository.getAllGroups()
    }
}
