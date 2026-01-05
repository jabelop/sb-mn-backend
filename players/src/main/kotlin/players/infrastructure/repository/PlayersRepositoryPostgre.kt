package com.jatec.players.infrastructure.repository

import com.jatec.persistence.tables.PlayersTable
import com.jatec.players.application.exceptions.PlayerExistingException
import com.jatec.players.domain.model.Player
import com.jatec.players.domain.options.WhereOptions
import com.jatec.players.domain.repository.PlayersRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID.fromString

class PlayersRepositoryPostgre(): PlayersRepository {

    fun ResultRow.toPlayer(): Player = Player(
        id = this[PlayersTable.id].toString(),
        name = this[PlayersTable.name],
    )

    override fun find(options: WhereOptions): List<Player> {
        return transaction {
            val query = PlayersTable.selectAll()
            options.playerId?.let {
                query.andWhere { PlayersTable.id.eq(fromString(it)) }
            }
            options.name?.let {
                query.andWhere { PlayersTable.name.eq(it) }
            }
            query.map { it.toPlayer() }.toList()
        }
    }

    override fun update(player: Player): Boolean {
        return false
    }

    override fun create(player: Player): Boolean {
        println("creating:")
        println(player)
        if (transaction {
            PlayersTable.select(PlayersTable.name).where {
                (PlayersTable.name.eq(player.name))
            }.toList().isNotEmpty()
        }) {
            throw PlayerExistingException("Player with name ${player.name} already exists")
        }

        transaction { PlayersTable.insert { it[id] = fromString(player.id); it[name] = player.name } }

        // error false
        return false
    }

    override fun delete(player: Player): Boolean {
        return false
    }
}