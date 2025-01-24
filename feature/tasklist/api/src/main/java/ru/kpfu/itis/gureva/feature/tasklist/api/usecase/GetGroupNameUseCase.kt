package ru.kpfu.itis.gureva.feature.tasklist.api.usecase

interface GetGroupNameUseCase {
    suspend operator fun invoke(id: Long): String?
}
