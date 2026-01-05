package com.jatec.players.application.service

import com.jatec.players.application.dtos.MessageOptions
import com.jatec.players.application.validators.OptionsValidatedBuilder
import com.jatec.players.application.validators.PlayerValidatedBuilder
import com.jatec.players.domain.model.Player
import com.jatec.players.domain.repository.PlayersRepository

class PlayersService(playersRepository: PlayersRepository) {
    val repository = playersRepository

    fun find(options: MessageOptions): List<Player> {
        return repository.find(OptionsValidatedBuilder.Companion.buildValidatedOptions(options))
    }

    fun update(player: Player): Boolean {
        return repository.update(PlayerValidatedBuilder.Companion.buildValidatePlayer(player));
    }

    fun create(player: Player): Boolean {
       return repository.create(PlayerValidatedBuilder.Companion.buildValidatePlayer(player))
    }

    fun delete(player: Player): Boolean {
        return repository.delete(PlayerValidatedBuilder.Companion.buildValidatePlayer(player))
    }

}