package com.example.braintrain.domain.usecase

import com.example.braintrain.domain.entity.GameSettings
import com.example.braintrain.domain.entity.Level
import com.example.braintrain.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}