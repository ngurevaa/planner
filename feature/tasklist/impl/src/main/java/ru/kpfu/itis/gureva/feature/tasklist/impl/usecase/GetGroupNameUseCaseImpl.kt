package ru.kpfu.itis.gureva.feature.tasklist.impl.usecase

import ru.kpfu.itis.gureva.feature.tasklist.api.repository.TaskListRepository
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetGroupNameUseCase
import javax.inject.Inject

class GetGroupNameUseCaseImpl @Inject constructor(
    private val groupRepository: TaskListRepository
) : GetGroupNameUseCase {
    override suspend fun invoke(id: Int): String? = groupRepository.getGroupName(id)
}
