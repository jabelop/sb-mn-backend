package com.jatec.combats.application.rules

import com.jatec.combats.domain.model.CombatData
import kotlin.collections.fold
import kotlin.collections.forEach
import kotlin.collections.plus
import kotlin.collections.reversed

class CombatMatcher {
    companion object {
        fun matchPlayersCreatures(combatData: List<CombatData>): List<CombatData> {
            val playersCreatures = combatData.groupBy { it.idPlayer }
            val player1 = combatData[0].idPlayer
            val player2 = combatData.find { it.idPlayer != player1 }!!.idPlayer
            var lessCreaturesId = if(playersCreatures[player1]!!.size < playersCreatures[player2]!!.size) player1 else player2
            var minCreatures = playersCreatures[lessCreaturesId]!!.size
            var maxCreaturesLevel = playersCreatures[lessCreaturesId]!!.fold(
                0,
                fun (acc: Int, cb: CombatData): Int {
                    return if (acc > cb.level) acc else cb.level
                }
            )

            var moreCreaturesId = if(lessCreaturesId == player1) player2 else player1
            val listToGet = mutableListOf<CombatData>()
            playersCreatures[moreCreaturesId]!!
                .reversed()
                .forEach { pc ->
                    if (pc.level <= maxCreaturesLevel && listToGet.size < minCreatures){
                        listToGet.add(pc)
                    }
                    else if (pc.level <= maxCreaturesLevel && Math.random() > 0.5){
                        listToGet.set(listToGet.size -1, pc)
                    }
                }

            if (listToGet.size < minCreatures) {
                playersCreatures.get(moreCreaturesId)!!.forEach { pc ->
                    if (!listToGet.contains(pc) && listToGet.size < minCreatures) listToGet.add(pc)
                }
            }

            return playersCreatures.get(lessCreaturesId)!! + listToGet
        }
    }
}