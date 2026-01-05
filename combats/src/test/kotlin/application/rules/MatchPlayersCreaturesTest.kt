package com.jatec.application.rules

import com.jatec.combats.application.rules.CombatMatcher
import com.jatec.combats.domain.model.CombatData
import org.junit.Test

class MatchPlayersCreaturesTest {

    @Test
    fun testMatchLowestLevelCreatures()  {
        val combatData: List<CombatData> = List<CombatData>(6, { i ->
            if (i < 2) CombatData(
                idCombat = "0",
                idPlayer = "1",
                id = "a-$i",
                name = "a-$i",
                creatureClass = "a-$i",
                level = i,
                xp = i +3,
                hp = 0 ,
                speed = 0,
                attack = 0,
                defense = 0,
                timeToAttack = 5
            )
            else CombatData(
                idCombat = "0",
                idPlayer = "2",
                id = "a-$i",
                name = "a-$i",
                creatureClass = "a-$i",
                level = i - 2,
                xp = i +1,
                hp = 0 ,
                speed = 0,
                attack = 0,
                defense = 0,
                timeToAttack = 5
            )
        })
        val result = CombatMatcher.matchPlayersCreatures(combatData)
        println(result)
        result.forEach { cb ->
            assert(cb.level < 2)
        }
        assert(result.size == 4)
    }

    @Test
    fun testMatchMidLevelCreatures()  {
        val combatData: List<CombatData> = List<CombatData>(6, { i ->
            if (i < 2) CombatData(
                idCombat = "0",
                idPlayer = "1",
                id = "a-$i",
                name = "a-$i",
                creatureClass = "a-$i",
                level = i+1,
                xp = i +3,
                hp = 0 ,
                speed = 0,
                attack = 0,
                defense = 0,
                timeToAttack = 5
            )
            else CombatData(
                idCombat = "0",
                idPlayer = "2",
                id = "a-$i",
                name = "a-$i",
                creatureClass = "a-$i",
                level = i - 2,
                xp = i +1,
                hp = 0 ,
                speed = 0,
                attack = 0,
                defense = 0,
                timeToAttack = 5
            )
        })
        val result = CombatMatcher.matchPlayersCreatures(combatData)
        println(result)
        result.forEach { cb ->
            assert(cb.level < 3)
        }
        assert(result.size == 4)
    }

}