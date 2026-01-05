package com.jatec.combats.infrastructure.repository

import com.jatec.combats.application.exceptions.CombatExistingException
import com.jatec.combats.application.exceptions.CombatNotExistingException
import com.jatec.combats.application.rules.CombatMatcher
import com.jatec.combats.application.rules.RunningCombatConstantsManger
import com.jatec.persistence.tables.CombatsTable
import com.jatec.persistence.tables.CombatsDataTable
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.model.CreatePlayerCombatData
import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.domain.repository.CombatsRepository
import com.jatec.shared.infrastructure.connection.ConnectionManagerKtor
import combats.domain.options.UpdatePlayerCombatOptions
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID.fromString
import kotlin.uuid.ExperimentalUuidApi

class CombatsRepositoryPostgre(): CombatsRepository {
    fun ResultRow.toCombat(): Combat = Combat(
        id = this[CombatsTable.id].toString(),
        idPlayer1 = this[CombatsTable.idPlayer1].toString(),
        idPlayer2 = this[CombatsTable.idPlayer2].toString(),
        winner = this[CombatsTable.winner]?.toString(),
        ip = this[CombatsTable.ip],
        port = this[CombatsTable.port],
        startedAt = this[CombatsTable.startedAt],
        updatedAt = this[CombatsTable.updatedAt],
        finishedAt = this[CombatsTable.finishedAt]
    )

    fun ResultRow.toCombatData(): CombatData = CombatData(
        idCombat = this[CombatsDataTable.idCombat].toString(),
        idPlayer = this[CombatsDataTable.idPlayer].toString(),
        id = this[CombatsDataTable.id].toString(),
        name = this[CombatsDataTable.name],
        creatureClass = this[CombatsDataTable.creatureClass],
        level = this[CombatsDataTable.level],
        xp = this[CombatsDataTable.xp],
        hp = this[CombatsDataTable.hp],
        speed = this[CombatsDataTable.speed],
        attack = this[CombatsDataTable.attack],
        defense = this[CombatsDataTable.defense],
        timeToAttack = this[CombatsDataTable.timeToAttack]
    )

    @OptIn(ExperimentalUuidApi::class)
    override fun find(options: WhereOptions): List<Combat> {
        println("In find with options")
        println(options)
        return transaction {
            val query = CombatsTable.selectAll()
            options.combatId?.let {
                query.andWhere { CombatsTable.id.eq(fromString(it)) }
            }
            options.playerId?.let {
                query.andWhere { CombatsTable.idPlayer1.eq(fromString(it)).or { CombatsTable.idPlayer2.eq(fromString(it)) } }
            }
            query.map { it.toCombat() }.toList()
        }
    }

    override fun findPlayerCombats(options: WhereOptions): List<CombatData> {
        println("In find Player Combats with options")
        println(options)
        return transaction {
            val query = CombatsDataTable.selectAll()
            options.playerId?.let {
                query.andWhere { CombatsDataTable.idPlayer.eq(fromString(it)) }
            }
            options.combatId?.let {
                query.andWhere { CombatsDataTable.idCombat.eq(fromString(it)) }
            }
            query.map { it.toCombatData() }.toList()
        }
    }

    override fun updatePlayerCombat(options: UpdatePlayerCombatOptions): CombatData {
        TODO("Not yet implemented")
    }

    override fun createPlayerCombat(options: CreatePlayerCombatData): CombatCreatedData {
        println("creating:")
        println("player1: ${options.player1Id} -> player2: ${options.player2Id}")
        return transaction {
            var combatRow: ResultRow? = null
            try {
                combatRow =
                    CombatsTable.selectAll()
                        .where {
                            CombatsTable.idPlayer1.eq(fromString(options.player1Id))
                                .and {
                                    CombatsTable.idPlayer2.eq(fromString(options.player2Id))
                                } .or {
                                    CombatsTable.idPlayer1.eq(fromString(options.player2Id))
                                        .and {
                                            CombatsTable.idPlayer2.eq(fromString(options.player1Id))
                                        }
                                }
                        }.first()
                // exposed method is not working, so it is checked here if there is some combat not finished
                if (combatRow.toCombat().finishedAt == null) {
                    throw CombatExistingException("The players ${options.player1Id} and ${options.player2Id} already are in a combat.")
                }
            } catch (_: NoSuchElementException) {
                // Do Nothing the combat can be created
            }
            CombatsTable.insert {
                    it[id] = fromString(options.combatData.get(0).idCombat)
                    it[idPlayer1] = fromString(options.player1Id)
                    it[idPlayer2] = fromString(options.player2Id)
                    it[ip] = ConnectionManagerKtor.currentIp!!
                    it[port] = ConnectionManagerKtor.currentPort!!
            }

            // match the creatures level of the players
            val combatDataToMatch = CombatMatcher.matchPlayersCreatures(options.combatData)
            println("matched creatures")
            println(combatDataToMatch)
            CombatsDataTable.batchInsert(combatDataToMatch) {
                        this[CombatsDataTable.idCombat] = fromString(it.idCombat)
                        this[CombatsDataTable.idPlayer] = fromString(it.idPlayer)
                        this[CombatsDataTable.id] =  fromString(it.id)
                        this[CombatsDataTable.name] = it.name
                        this[CombatsDataTable.creatureClass] = it.creatureClass
                        this[CombatsDataTable.level] = it.level
                        this[CombatsDataTable.xp] = it.xp
                        this[CombatsDataTable.hp] = it.hp
                        this[CombatsDataTable.speed] = it.speed
                        this[CombatsDataTable.attack] = it.attack
                        this[CombatsDataTable.defense] = it.defense
                        this[CombatsDataTable.timeToAttack] = RunningCombatConstantsManger.getMaxTimeToAttack()

            }
            val today = Calendar.getInstance()
            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(today.time)
            CombatCreatedData(
                combat = Combat(
                    id = options.combatData.get(0).idCombat,
                    idPlayer1 = options.player1Id,
                    idPlayer2 = options.player2Id,
                    winner = null,
                    ip = ConnectionManagerKtor.currentIp!!,
                    port = ConnectionManagerKtor.currentPort!!,
                    startedAt = now,
                    updatedAt = now,
                    finishedAt = null
                ),
                combatData = combatDataToMatch
            )
        }
    }

    override fun findCombatCreatedData(id: String): CombatCreatedData {
        return transaction {
            try {
                val combat = CombatsTable
                    .selectAll()
                    .where {
                        CombatsTable.id.eq(fromString(id))
                    }.first().toCombat()
                val combatData = CombatsDataTable
                    .selectAll()
                    .where {
                        CombatsDataTable.idCombat.eq(fromString(id))
                    }.map { it.toCombatData() }
                CombatCreatedData(
                    combat = combat,
                    combatData = combatData,
                )
            } catch (_: NoSuchElementException) {
                throw CombatNotExistingException("The combat with id: $id does not exist")
            }
        }
    }

    override fun delete(combat: Combat): Boolean {
        return false
    }
}