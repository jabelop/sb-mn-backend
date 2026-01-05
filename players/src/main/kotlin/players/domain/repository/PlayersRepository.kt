package com.jatec.players.domain.repository

import com.jatec.players.domain.model.Player
import com.jatec.players.domain.options.WhereOptions

interface PlayersRepository {

    fun find(options: WhereOptions): List<Player>

    fun update(player: Player): Boolean

    fun create(player: Player): Boolean

    fun delete(player: Player): Boolean
}