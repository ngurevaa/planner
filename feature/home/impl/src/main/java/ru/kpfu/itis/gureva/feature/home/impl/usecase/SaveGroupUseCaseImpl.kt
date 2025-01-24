package ru.kpfu.itis.gureva.feature.home.impl.usecase

import ru.kpfu.itis.gureva.core.utils.ResourceManager
import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import ru.kpfu.itis.gureva.feature.home.impl.R
import ru.kpfu.itis.gureva.feature.home.impl.exception.AlreadyExistingGroupNameException
import ru.kpfu.itis.gureva.feature.home.impl.exception.EmptyGroupNameException
import javax.inject.Inject

class SaveGroupUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository,
    private val resourceManager: ResourceManager
) : SaveGroupUseCase {
    override suspend fun invoke(name: String): Long {
        if (name.isEmpty()) {
            throw EmptyGroupNameException(resourceManager.getString(R.string.empty_group_name_error))
        }
        if (homeRepository.getGroupByName(name.trim()) != null) {
            throw AlreadyExistingGroupNameException(resourceManager.getString(R.string.existing_group_name_error))
        }

        return homeRepository.saveGroup(name.trim())
    }
}
