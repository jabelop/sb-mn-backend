package com.jatec.infrastructure

import com.jatec.combats.application.dtos.PlayerAction
import com.jatec.combats.application.rules.CombatManager
import com.jatec.combats.application.rules.CombatRunningActions
import com.jatec.combats.domain.constants.TIME_TO_ATTACK
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.infrastructure.rules.CombatManagerKtor
import org.junit.Test
import kotlin.time.TimeSource

class CombatManagerTest {

    fun buildCombatData(data: List<CombatData>): List<CombatData> {
        return data.map { CombatData(
            idCombat = it.idCombat,
            idPlayer = it.idPlayer,
            id = it.id,
            name = it.name,
            creatureClass = it.creatureClass,
            level = it.level,
            xp = it.xp,
            hp = it.hp,
            speed = it.speed,
            attack = it.attack,
            defense = it.defense,
            timeToAttack = it.timeToAttack
        ) }
    }

    var initialCombatData = listOf<CombatData>(
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
        )
    val combatManager: CombatManager = CombatManagerKtor(
        initCombat = Combat(
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
        initCombatData = buildCombatData(initialCombatData)
    )

    var nextCreature: CombatData? = null

    @Test
    fun testFirstUpdateCombat() {
        var updating = true
        var iteration = 1
        var updatedCombatData: List<CombatData>? = null
        while(updating) {
           updatedCombatData = combatManager.updateLoop(TimeSource.Monotonic.markNow())
           updatedCombatData.forEach {
               val newExpectedTimeToAttack = initialCombatData
                   .find { icd -> icd.id == it.id }!!
                   .timeToAttack - (iteration * it.speed)

               assert(it.timeToAttack == newExpectedTimeToAttack)
               if (it.timeToAttack <= 0) updating = false
           }
            iteration += 1
        }
        initialCombatData = buildCombatData(updatedCombatData!!)
        testGetFirstNextCreatureToPlayOrNull()
    }

    fun testGetFirstNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.first().id)
        testPerformFirstActionP1()
    }

    fun testPerformFirstActionP1() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.last().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.first().timeToAttack == TIME_TO_ATTACK)
        val damage = nextCreature!!.attack - initialCombatData.last().defense
        assert(updatedCombatData.last().hp == initialCombatData.last().hp - damage && updatedCombatData.last().defense == 0)
        initialCombatData = buildCombatData(updatedCombatData)
        testGetSecondNextCreatureToPlayOrNull()
    }

    fun testGetSecondNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.last().id)
        testPerformFirstActionP2()
    }

    fun testPerformFirstActionP2() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.first().id
            ),
            source = nextCreature!!
        )

        val damage = nextCreature!!.attack - initialCombatData.first().defense
        assert(updatedCombatData.last().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.first().hp == initialCombatData.first().hp - damage && updatedCombatData.first().defense == 0)
        initialCombatData = buildCombatData(updatedCombatData)
        testSecondUpdateCombat()
    }



    fun testSecondUpdateCombat() {
        var updating = true
        var iteration = 1
        var updatedCombatData: List<CombatData>? = null
        while(updating) {
            updatedCombatData = combatManager.updateLoop(TimeSource.Monotonic.markNow())
            updatedCombatData.forEach {
                val newExpectedTimeToAttack = initialCombatData
                    .find { icd -> icd.id == it.id }!!
                    .timeToAttack - (iteration * it.speed)

                assert(it.timeToAttack == newExpectedTimeToAttack)
                if (it.timeToAttack <= 0) updating = false
            }
            iteration += 1
        }
        initialCombatData = buildCombatData(updatedCombatData!!)
        testGetThirdNextCreatureToPlayOrNull()
    }

    fun testGetThirdNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.first().id)
        testPerformSecondActionP1()
    }

    fun testPerformSecondActionP1() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.last().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.first().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.last().hp == initialCombatData.last().hp - nextCreature!!.attack)
        initialCombatData = buildCombatData(updatedCombatData)
        testGetFourthNextCreatureToPlayOrNull()
    }

    fun testGetFourthNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.last().id)
        testPerformSecondActionP2()
    }

    fun testPerformSecondActionP2() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.DEFENSE_RECEIVED,
                sourceId = nextCreature!!.id,
                targetId = nextCreature!!.id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.last().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.last().defense == 1)
        initialCombatData = buildCombatData(updatedCombatData)
        testThirdUpdateCombat()
    }

    fun testThirdUpdateCombat() {
        var updating = true
        var iteration = 1
        var updatedCombatData: List<CombatData>? = null
        while(updating) {
            updatedCombatData = combatManager.updateLoop(TimeSource.Monotonic.markNow())
            updatedCombatData.forEach {
                val newExpectedTimeToAttack = initialCombatData
                    .find { icd -> icd.id == it.id }!!
                    .timeToAttack - (iteration * it.speed)

                assert(it.timeToAttack == newExpectedTimeToAttack)
                if (it.timeToAttack <= 0) updating = false
            }
            iteration += 1
        }
        initialCombatData = buildCombatData(updatedCombatData!!)
        testGetFifthNextCreatureToPlayOrNull()
    }

    fun testGetFifthNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.first().id)
        testPerformThirdActionP1()
    }

    fun testPerformThirdActionP1() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.last().id
            ),
            source = nextCreature!!
        )

        val damage = nextCreature!!.attack - initialCombatData.last().defense
        assert(updatedCombatData.first().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.last().hp == initialCombatData.last().hp - damage && updatedCombatData.last().defense == 0)
        initialCombatData = buildCombatData(updatedCombatData)
        testGetSixthNextCreatureToPlayOrNull()
    }

    fun testGetSixthNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.last().id)
        testPerformThirdActionP2()
    }

    fun testPerformThirdActionP2() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.first().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.last().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.first().hp == initialCombatData.first().hp - nextCreature!!.attack)
        initialCombatData = buildCombatData(updatedCombatData)
        testFourthUpdateCombat()
    }

    fun testFourthUpdateCombat() {
        var updating = true
        var iteration = 1
        var updatedCombatData: List<CombatData>? = null
        while(updating) {
            updatedCombatData = combatManager.updateLoop(TimeSource.Monotonic.markNow())
            updatedCombatData.forEach {
                val newExpectedTimeToAttack = initialCombatData
                    .find { icd -> icd.id == it.id }!!
                    .timeToAttack - (iteration * it.speed)

                assert(it.timeToAttack == newExpectedTimeToAttack)
                if (it.timeToAttack <= 0) updating = false
            }
            iteration += 1
        }
        initialCombatData = buildCombatData(updatedCombatData!!)
        testGetSeventhNextCreatureToPlayOrNull()
    }

    fun testGetSeventhNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.first().id)
        testPerformFourthActionP1()
    }

    fun testPerformFourthActionP1() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.last().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.first().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.last().hp == initialCombatData.last().hp - nextCreature!!.attack)
        initialCombatData = buildCombatData(updatedCombatData)
        testCheckFirstPlayer1Lost()
    }

    fun testCheckFirstPlayer1Lost() {
        assert(!combatManager.checkLoser(initialCombatData.first().idPlayer))
        testCheckFirstPlayer2Lost()
    }

    fun testCheckFirstPlayer2Lost() {
        assert(!combatManager.checkLoser(initialCombatData.last().idPlayer))
        testGetEighthNextCreatureToPlayOrNull()
    }

    fun testGetEighthNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.last().id)
        testPerformFourthActionP2()
    }

    fun testPerformFourthActionP2() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.first().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.last().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.first().hp == initialCombatData.first().hp - nextCreature!!.attack)
        initialCombatData = buildCombatData(updatedCombatData)
        testFifthUpdateCombat()
    }

    fun testFifthUpdateCombat() {
        var updating = true
        var iteration = 1
        var updatedCombatData: List<CombatData>? = null
        while(updating) {
            updatedCombatData = combatManager.updateLoop(TimeSource.Monotonic.markNow())
            updatedCombatData.forEach {
                val newExpectedTimeToAttack = initialCombatData
                    .find { icd -> icd.id == it.id }!!
                    .timeToAttack - (iteration * it.speed)

                assert(it.timeToAttack == newExpectedTimeToAttack)
                if (it.timeToAttack <= 0) updating = false
            }
            iteration += 1
        }
        initialCombatData = buildCombatData(updatedCombatData!!)
        testGetNinthNextCreatureToPlayOrNull()
    }

    fun testGetNinthNextCreatureToPlayOrNull() {
        nextCreature = combatManager.getNextCreatureToPlayOrNull()
        assert(nextCreature != null)
        assert(nextCreature!!.id == initialCombatData.first().id)
        testPerformFifthActionP1()
    }

    fun testPerformFifthActionP1() {
        val updatedCombatData = combatManager.performAction(
            action = PlayerAction(
                action = CombatRunningActions.CAUSED_DAMAGE,
                sourceId = nextCreature!!.id,
                targetId = initialCombatData.last().id
            ),
            source = nextCreature!!
        )

        assert(updatedCombatData.first().timeToAttack == TIME_TO_ATTACK)
        assert(updatedCombatData.last().hp == initialCombatData.last().hp - nextCreature!!.attack)
        initialCombatData = buildCombatData(updatedCombatData)
        testCheckSecondPlayer1Lost()
    }

    fun testCheckSecondPlayer1Lost() {
        assert(!combatManager.checkLoser(initialCombatData.first().idPlayer))
        testCheckSecondPlayer2Lost()
    }

    fun testCheckSecondPlayer2Lost() {
        assert(combatManager.checkLoser(initialCombatData.last().idPlayer))
    }
}
