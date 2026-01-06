package com.jatec.repository

import com.jatec.combats.application.exceptions.CombatExistingException
import com.jatec.combats.application.exceptions.CombatNotExistingException
import com.jatec.combats.domain.constants.TIME_TO_ATTACK
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.model.CreatePlayerCombatData
import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.domain.repository.CombatsRepository
import combats.domain.options.UpdatePlayerCombatOptions


class CombatsRepositoryTest: CombatsRepository {
    var combats = listOf<Combat>(
        Combat(
            id = "05fb3246-9387-4d04-a27f-fab107c33883",
            idPlayer1 = "88db3246-9387-4d04-a27f-fab107c33800",
            idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
            winner = null,
            ip = "0.0.0.0",
            port = 6587,
            startedAt = "2025-12-17T21:30:23",
            updatedAt = "2025-12-17T23:15:23",
            finishedAt = null
        ),
        Combat(
            id = "05fb32c6-9387-4744-a27f-fab107c33883",
            idPlayer1 = "88db3246-9387-4104-a23f-fab107c33800",
            idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
            winner = "88db3246-9387-4104-a23f-fab107c33800",
            ip = "0.0.0.1",
            port = 6587,
            startedAt = "2025-12-18T01:30:23",
            updatedAt = "2025-12-18T12:15:23",
            finishedAt = 1767724807040,
        )
    )
    var combatsData = listOf<CombatData>(
        CombatData(
            idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
            idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
            id = "77db3246-9387-4d04-a27f-5ab123c338aa",
            name = "Creature 1",
            creatureClass = "warrior",
            level = 1,
            xp = 123,
            hp = 130,
            speed = 26,
            attack = 32,
            defense = 2,
            timeToAttack = 24
        ),
        CombatData(
            idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
            idPlayer = "88dba046-93b7-4d04-a27f-fab107c33800",
            id = "77db3246-9387-4d04-a27f-5ab123c228aa",
            name = "Creature 3",
            creatureClass = "enchanter",
            level = 1,
            xp = 133,
            hp = 132,
            speed = 34,
            attack = 26,
            defense = 2,
            timeToAttack = 18
        ),
        CombatData(
            idCombat = "05fb32c6-9387-4744-a27f-fab107c33883",
            idPlayer = "88dba046-93b7-4d04-a27f-fab107c33800",
            id = "77db3246-9387-4d04-a27f-5ab123c338aa",
            name = "Creature 1",
            creatureClass = "warrior",
            level = 3,
            xp = 312,
            hp = 0,
            speed = 37,
            attack = 30,
            defense = 3,
            timeToAttack = 32
        ),
        CombatData(
            idCombat = "05fb32c6-9387-4744-a27f-fab107c33883",
            idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
            id = "77db3246-9387-4d04-a27f-5ab123c228aa",
            name = "Creature 1",
            creatureClass = "enchanter",
            level = 3,
            xp = 316,
            hp = 170,
            speed = 43,
            attack = 27,
            defense = 3,
            timeToAttack = 14
        )
    )
    override fun find(options: WhereOptions): List<Combat> {
        var filteredCombats: List<Combat> = combats.map { Combat(
            id = it.id,
            idPlayer1 = it.idPlayer1,
            idPlayer2 = it.idPlayer2,
            winner = it.winner,
            ip = it.ip,
            port = it.port,
            startedAt = it.startedAt,
            updatedAt = it.updatedAt,
            finishedAt = it.finishedAt
        ) }
        options.playerId?.let {
            filteredCombats = combats.filter {
                it.idPlayer1.equals(options.playerId) ||
                        it.idPlayer2.equals(options.playerId)
            }
        }
        options.combatId?.let {
            filteredCombats = filteredCombats.filter {
                it.id.equals(options.combatId)
            }
        }
        return filteredCombats
    }

    override fun findPlayerCombats(options: WhereOptions): List<CombatData> {
        var filteredCombatsData: List<CombatData> = combatsData.filter { true }
        options.playerId?.let {
            filteredCombatsData = combatsData.filter {
                it.idPlayer.equals(options.playerId)
            }
        }
        options.combatId?.let {
            filteredCombatsData = filteredCombatsData.filter {
                it.idCombat.equals(options.combatId)
            }
        }
        return filteredCombatsData
    }

    override fun updatePlayerCombat(combat: Combat, combatData: List<CombatData>) {
        TODO("Not yet implemented")
    }

    override fun createPlayerCombat(options: CreatePlayerCombatData): CombatCreatedData {
       val combat = combats.find {
           ((it.idPlayer1.equals(options.player1Id) && it.idPlayer2.equals(options.player2Id))
                   || (it.idPlayer1.equals(options.player2Id) && it.idPlayer2.equals(options.player1Id)))
                   && (it.finishedAt != null)
       }
        if (combat != null) throw CombatExistingException("")
        val newCombat = Combat(
            id = "444${options.player1Id.slice(IntRange(3, options.player1Id.length - 1))}",
            idPlayer1 = options.player1Id,
            idPlayer2 = options.player2Id,
            winner = null,
            ip = "0.0.0.0",
            port = 6654,
            startedAt = "2025-12-18T01:30:23",
            updatedAt = "2025-12-18T12:15:23",
            finishedAt = null
        )
        combats = combats.plus(newCombat)
        var combatData: List<CombatData> = emptyList()
        options.combatData.forEach {
            combatData = combatData.plus(
                CombatData(
                    idCombat = "444${options.player1Id.slice(IntRange(3, options.player1Id.length - 1))}",
                    idPlayer = options.player1Id,
                    id = it.id,
                    name = it.name,
                    creatureClass = it.creatureClass,
                    level = it.level,
                    xp = it.xp,
                    hp = it.hp,
                    speed = it.speed,
                    attack = it.attack,
                    defense = it.defense,
                    timeToAttack = TIME_TO_ATTACK
                )
            )
        }

        return CombatCreatedData(
            combat = newCombat,
            combatData = combatData
        )
    }

    override fun findCombatCreatedData(id: String): CombatCreatedData {
        val combat = combats.find {
                it.id.equals(id)
        }
        if (combat == null) throw CombatNotExistingException("")
        val combatData = combatsData.filter {
                it.idCombat.equals(id)
        }
        return CombatCreatedData(
            combat = combat,
            combatData = combatData,
        )
    }

    override fun delete(combat: Combat): Boolean {
        return true
    }
}