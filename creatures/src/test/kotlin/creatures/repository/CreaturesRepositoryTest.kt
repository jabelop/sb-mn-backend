package com.jatec.creatures.repository

import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.options.WhereOptions
import com.jatec.creatures.domain.repository.CreaturesRepository
import com.jatec.creatures.domain.rules.LevelUpManager
import creatures.application.exceptions.CreatureNotExistingException
import creatures.domain.options.UpdatePlayerCreatureOptions


class CreaturesRepositoryTest(levelUpManager: LevelUpManager): CreaturesRepository {
    val lvlUpManager = levelUpManager
    val creatures = listOf<Creature>(
        Creature(
            id = "05fb3246-9387-4d04-a27f-fab107c33883",
            name = "Creature test 1",
            creatureClass = "warrior",
            level = 0,
            xp = 89,
            hp = 100,
            speed = 15,
            attack = 18
        ),
        Creature(
            id="05fb3246-9387-4d04-a2cf-fa0107433883",
            name = "Creature test 2",
            creatureClass = "warrior",
            level = 1,
            xp = 167,
            hp = 115,
            speed = 20,
            attack = 24
        ),
        Creature(
            id="88fb3246-9387-4d04-a2cf-fa0107433883",
            name = "Creature test 3",
            creatureClass = "defender",
            level = 0,
            xp = 46,
            hp = 88,
            speed = 13,
            attack = 10
        ),
        Creature(
            id="a7fb3246-0087-4d04-a2cf-fa0107433883",
            name = "Creature test 4",
            creatureClass = "enchanter",
            level = 1,
            xp = 196,
            hp = 105,
            speed = 18,
            attack = 22
        )
    )

    var playerCreatures = listOf<PlayerCreature>(
        PlayerCreature(
            id = "05fb3246-9387-4d04-a27f-fab107c33883",
            name = "Creature test 1",
            idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
            creatureClass = "warrior",
            level = 0,
            xp = 89,
            hp = 100,
            speed = 15,
            attack = 18,
        ),
        PlayerCreature(
            id="05fb3246-9387-4d04-a2cf-fa0107433883",
            name = "Creature test 2",
            idPlayer = "05c93246-9387-4d04-a27f-fab107c33883",
            creatureClass = "warrior",
            level = 1,
            xp = 167,
            hp = 115,
            speed = 20,
            attack = 24
        ),
        PlayerCreature(
            id="88fb3246-9387-4d04-a2cf-fa0107433883",
            name = "Creature test 3",
            idPlayer = "05c93246-9387-4d04-a27f-fab107c33883",
            creatureClass = "defender",
            level = 0,
            xp = 46,
            hp = 88,
            speed = 13,
            attack = 10
        ),
        PlayerCreature(
            id="a7fb3246-0087-4d04-a2cf-fa0107433883",
            name = "Creature test 4",
            idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
            creatureClass = "enchanter",
            level = 1,
            xp = 196,
            hp = 105,
            speed = 18,
            attack = 22
        )
    )

    override fun find(options: WhereOptions): List<Creature> {
        var allCreatures = creatures.toList()

        options.creatureId?.let {
            allCreatures = allCreatures.filter { it.id.equals(options.creatureId) }
        }

        options.name?.let {
            allCreatures = allCreatures.filter { it.name.equals(options.name) }
        }

        return allCreatures
    }

    override fun findPlayerCreatures(options: WhereOptions): List<PlayerCreature> {
        var allPlayerCreatures = playerCreatures.toList()
        options.name?.let {
            allPlayerCreatures = allPlayerCreatures.filter { it.name.equals(options.name) }
        }
        if (options.playerId != null) {
            allPlayerCreatures = if (options.enemyId != null) {
                allPlayerCreatures.filter { it.idPlayer.equals(options.playerId) || it.idPlayer.equals(options.enemyId) }
            } else {
                allPlayerCreatures.filter { it.idPlayer.equals(options.playerId) }
            }
        }

        return allPlayerCreatures
    }

    override fun updatePlayerCreature(options: UpdatePlayerCreatureOptions): PlayerCreature {
        var playerCreature = playerCreatures.toList().find { it.id.equals(options.creatureId) }
        var newPlayerCreature: PlayerCreature? = null
        try {
            newPlayerCreature = lvlUpManager.increaseXp(playerCreature!!, options.xp)

            playerCreatures =  playerCreatures.map { if(it.id == playerCreature.id) newPlayerCreature else it }
            return newPlayerCreature
        } catch (_: NullPointerException) {
            throw CreatureNotExistingException("The creature id: ${options.creatureId}, is invalid")
        }
    }

    override fun createPlayerCreature(options: CreatePlayerCreatureOptions): Boolean {
        val creature = this.creatures.find {it.id.equals(options.creatureId)}
        try {
            playerCreatures.plus(
                PlayerCreature(
                    id = creature!!.id,
                    name = creature.name,
                    creatureClass = creature.creatureClass,
                    idPlayer = options.playerId,
                    level = creature.level,
                    xp = creature.xp,
                    hp = creature.hp,
                    speed = creature.speed,
                    attack = creature.attack
                )
            )

            return true
        } catch (_: NullPointerException) {
            throw CreatureNotExistingException("The creature id: ${options.creatureId}, is invalid")
        }
    }

    override fun delete(creature: Creature): Boolean {
        return false
    }

}