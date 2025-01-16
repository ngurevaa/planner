package ru.kpfu.itis.gureva.feature.home.api.usecase

interface SaveGroupUseCase {
    suspend operator fun invoke(name: String)
}
