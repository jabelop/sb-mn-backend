package com.jatec.creatures.application.service

import com.jatec.creatures.application.dtos.MessageOptions
import com.jatec.creatures.application.validators.CreatePlayerCreatureOptionsValidatedBuilder
import com.jatec.creatures.application.validators.CreatureValidatedBuilder
import com.jatec.creatures.application.validators.OptionsValidatedBuilder
import com.jatec.creatures.application.validators.UpdatePlayerCreatureOptionsValidatedBuilder
import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.repository.CreaturesRepository
import creatures.domain.options.UpdatePlayerCreatureOptions

class CreaturesService(creaturesRepository: CreaturesRepository) {
    val repository = creaturesRepository

    fun find(options: MessageOptions): List<Creature> {
        return repository.find(OptionsValidatedBuilder.Companion.buildValidatedOptions(options))
    }

    fun findPlayerCreatures(options: MessageOptions): List<PlayerCreature> {
        return repository.findPlayerCreatures(OptionsValidatedBuilder.Companion.buildValidatedOptions(options))
    }

    fun updatePlayerCreature(options: UpdatePlayerCreatureOptions): PlayerCreature {
        return repository.updatePlayerCreature(
            UpdatePlayerCreatureOptionsValidatedBuilder.Companion.buildValidatedUpdatePlayerCreatureOptions(options)
        )
    }

    fun create(options: CreatePlayerCreatureOptions): Boolean {
       return repository.createPlayerCreature(
           CreatePlayerCreatureOptionsValidatedBuilder.Companion.buildValidatedCreatePlayerCreatureOptions(options)
       )
    }

    fun delete(creature: Creature): Boolean {
        return repository.delete(CreatureValidatedBuilder.Companion.buildValidatedCreature(creature))
    }

}