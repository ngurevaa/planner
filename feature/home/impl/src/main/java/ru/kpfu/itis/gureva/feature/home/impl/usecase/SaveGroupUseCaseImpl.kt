package ru.kpfu.itis.gureva.feature.home.impl.usecase

import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import javax.inject.Inject

class SaveGroupUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository
) : SaveGroupUseCase {
    override suspend fun invoke(name: String) {
        homeRepository.saveGroup(name)
    }
}
