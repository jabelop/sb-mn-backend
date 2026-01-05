package com.jatec.players.repository

import com.jatec.players.application.exceptions.PlayerExistingException
import com.jatec.players.domain.model.Player
import com.jatec.players.domain.options.WhereOptions
import com.jatec.players.domain.repository.PlayersRepository


class PlayersRepositoryTest(): PlayersRepository {
    val players = listOf<Player>(
        Player(id="05fb3246-9387-4d04-a27f-fab107c33883", name = "Player test 1"),
        Player(id="05fb3246-9387-4d04-a2cf-fa0107433883", name = "Player test 2")
    )

    override fun find(options: WhereOptions): List<Player> {
       var allPlayers = players.toList()
        options.playerId?.let {
            allPlayers = allPlayers.filter {
                it.id.equals(options.playerId)
            }
        }
        options.name?.let {
            allPlayers = allPlayers.filter { it.name.equals(options.name) }
        }
        return allPlayers
    }

    override fun update(player: Player): Boolean {
        return true
    }

    override fun create(player: Player): Boolean {
        if (players.any { it.name === player.name}) {
            throw PlayerExistingException("")
        }
        players.plus(player)
        return true
    }

    override fun delete(player: Player): Boolean {
        return true
    }
}