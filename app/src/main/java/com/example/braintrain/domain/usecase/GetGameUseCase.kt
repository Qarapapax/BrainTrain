package com.example.braintrain.domain.usecase

import com.example.braintrain.domain.repository.GameRepository

class GetGameUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}