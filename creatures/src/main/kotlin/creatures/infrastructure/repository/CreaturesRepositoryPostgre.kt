package com.jatec.creatures.infrastructure.repository

import com.jatec.creatures.application.exceptions.PlayerCreatureExistingException
import com.jatec.persistence.tables.CreaturesTable
import com.jatec.persistence.tables.PlayerCreaturesTable
import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.options.WhereOptions
import com.jatec.creatures.domain.repository.CreaturesRepository
import com.jatec.creatures.domain.rules.LevelUpManager
import creatures.application.exceptions.CreatureNotExistingException
import creatures.domain.options.UpdatePlayerCreatureOptions
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID
import java.util.UUID.fromString
import kotlin.uuid.ExperimentalUuidApi

class CreaturesRepositoryPostgre(levelUpManager: LevelUpManager): CreaturesRepository {
    val lManager = levelUpManager
    fun ResultRow.toCreature(): Creature = Creature(
        id = this[CreaturesTable.id].toString(),
        name = this[CreaturesTable.name],
        creatureClass = this[CreaturesTable.creatureClass],
        level = this[CreaturesTable.level],
        xp = this[CreaturesTable.xp],
        hp = this[CreaturesTable.hp],
        speed = this[CreaturesTable.speed],
        attack = this[CreaturesTable.attack]
    )

    fun ResultRow.toPlayerCreature(): PlayerCreature = PlayerCreature(
        id = this[PlayerCreaturesTable.id].toString(),
        name = this[PlayerCreaturesTable.name],
        creatureClass = this[PlayerCreaturesTable.creatureClass],
        idPlayer = this[PlayerCreaturesTable.idPlayer].toString(),
        level = this[PlayerCreaturesTable.level],
        xp = this[PlayerCreaturesTable.xp],
        hp = this[PlayerCreaturesTable.hp],
        speed = this[PlayerCreaturesTable.speed],
        attack = this[PlayerCreaturesTable.attack]
    )

    @OptIn(ExperimentalUuidApi::class)
    override fun find(options: WhereOptions): List<Creature> {
        println("In find with options")
        println(options)
        return transaction {
            val query = CreaturesTable.selectAll()
            options.creatureId?.let {
                query.andWhere { CreaturesTable.id.eq(fromString(it)) }
            }
            options.name?.let {
                query.andWhere { CreaturesTable.name.eq(it) }
            }
            query.map { it.toCreature() }.toList()
        }
    }

    override fun findPlayerCreatures(options: WhereOptions): List<PlayerCreature> {
        return transaction {
            val query = PlayerCreaturesTable
                .select(
                    PlayerCreaturesTable.id,
                    PlayerCreaturesTable.name,
                    PlayerCreaturesTable.creatureClass,
                    PlayerCreaturesTable.idPlayer,
                    PlayerCreaturesTable.level,
                    PlayerCreaturesTable.xp,
                    PlayerCreaturesTable.hp,
                    PlayerCreaturesTable.speed,
                    PlayerCreaturesTable.attack
                )
            options.name?.let {
                query.andWhere { PlayerCreaturesTable.name.eq(it) }
            }
            if (options.playerId != null) {
                if (options.enemyId != null) {
                    query.andWhere {
                        PlayerCreaturesTable.idPlayer.eq(fromString(options.playerId)).or {
                            PlayerCreaturesTable.idPlayer.eq(fromString(options.enemyId))
                        }
                    }
                } else {
                    query.andWhere {
                        PlayerCreaturesTable.idPlayer.eq(fromString(options.playerId))
                    }
                }
            }
            query.map { it.toPlayerCreature() }.toList()
        }
    }

    override fun updatePlayerCreature(options: UpdatePlayerCreatureOptions): PlayerCreature {
        return transaction {
            var creatureRow: ResultRow? = null
            try {
                creatureRow =
                    PlayerCreaturesTable.selectAll()
                        .where { PlayerCreaturesTable.id.eq(fromString(options.creatureId)) }.first()
            } catch (_: NoSuchElementException) {
               throw CreatureNotExistingException("The creature id: ${options.creatureId}, is invalid")
            }
            val updatedPlayerCreature = lManager.increaseXp(
                creatureRow!!.toPlayerCreature(),
                options.xp
            )

            PlayerCreaturesTable.update {
                it[level] = updatedPlayerCreature.level
                it[xp] = updatedPlayerCreature.xp
                it[hp] = updatedPlayerCreature.hp
                it[speed] = updatedPlayerCreature.speed
                it[attack] = updatedPlayerCreature.attack
            }
            updatedPlayerCreature
        }
    }

    override fun createPlayerCreature(options: CreatePlayerCreatureOptions): Boolean {
        println("creating:")
        println("player: ${options.playerId} -> creature: ${options.creatureId}")
        return transaction {
            var creatureRow: ResultRow? = null
            try {
                creatureRow =
                    CreaturesTable.selectAll()
                        .where { CreaturesTable.id.eq(fromString(options.creatureId)) }.first()
            } catch (_: NoSuchElementException) {
                throw CreatureNotExistingException("The creature id: ${options.creatureId}, is invalid")
            }
            val creature = creatureRow!!.toCreature()

            val playerCreatureQuery =  PlayerCreaturesTable.select(PlayerCreaturesTable.idPlayer)
                .andWhere {
                    (PlayerCreaturesTable.idPlayer.eq(fromString(options.playerId)))

                }
                .andWhere {
                    (PlayerCreaturesTable.name.eq(creature.name))
                }
                .toList()
            if (playerCreatureQuery.isNotEmpty()) {
                throw PlayerCreatureExistingException("The player ${options.playerId} already has the creature ${options.creatureId}")
            }
                PlayerCreaturesTable.insert {
                    it[id] = UUID.randomUUID()
                    it[idPlayer] = fromString(options.playerId)
                    it[name] = creature.name
                    it[creatureClass] = creature.creatureClass
                    it[level] = creature.level
                    it[hp] = creature.hp
                    it[xp] = creature.xp
                    it[speed] = creature.speed
                    it[attack] = creature.attack
                }
            // error false
            false
        }
    }

    override fun delete(creature: Creature): Boolean {
        return false
    }
}