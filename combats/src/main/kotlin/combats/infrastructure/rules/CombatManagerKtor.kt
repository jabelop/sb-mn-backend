package com.jatec.combats.infrastructure.rules

import com.jatec.combats.application.dtos.PlayerAction
import com.jatec.combats.application.rules.CombatManager
import com.jatec.combats.application.rules.RunningCombatConstantsManger
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.application.rules.CombatRunningActions
import io.ktor.websocket.DefaultWebSocketSession
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

class CombatManagerKtor(val initCombat: Combat, val initCombatData: List<CombatData>): CombatManager {
    val combat: Combat = initCombat
    val combatData: List<CombatData> = initCombatData

    companion object {
        val runningCombats = HashMap<String, HashMap<String, DefaultWebSocketSession>>()
    }

    override fun updateLoop(startPeriod: TimeSource.Monotonic.ValueTimeMark): List<CombatData> {
        var elapsed = startPeriod.elapsedNow().inWholeSeconds
        while( elapsed < 1 ) {
            elapsed = startPeriod.elapsedNow().inWholeSeconds
        }
        this.combatData.forEach { combatD ->
            if (combatD.hp <= 0) return@forEach

            combatD.timeToAttack -= if (combatD.timeToAttack <= 0) 0 else combatD.speed

        }
        return this.combatData
    }

    override fun getNextCreatureToPlayOrNull(): CombatData? {
        return this.combatData.find { it.timeToAttack <= 0 }
    }

    override fun performAction(action: PlayerAction, source: CombatData): List<CombatData> {
        var target: CombatData? = null
        this.combatData.forEach {
            target = if(it.id == action.targetId) it else target
        }
        val invalidTarget = target == null
                || (target == source && action.action != CombatRunningActions.Companion.DEFENSE_RECEIVED)

        if (invalidTarget) return this.combatData

        else {
            val damage = source.attack - target.defense
            val isAttackReceived = action.action == CombatRunningActions.Companion.CAUSED_DAMAGE
            target.defense -= if(isAttackReceived) target.defense else 0
            target.hp = if(isAttackReceived && target.hp > 0) target.hp - damage else target.hp
            target.defense = if(action.action == CombatRunningActions.Companion.DEFENSE_RECEIVED) target.defense + 1 else target.defense
        }
        source.timeToAttack = RunningCombatConstantsManger.Companion.getMaxTimeToAttack()
        return this.combatData
    }

    override fun getAIAction(): String {
       return if (Math.random() > RunningCombatConstantsManger.Companion.getChooseDefenseThresholdAi()) CombatRunningActions.Companion.DEFENSE_RECEIVED
       else CombatRunningActions.Companion.CAUSED_DAMAGE
    }

    override fun getAITarget(idAI: String, action: String): CombatData? {
        var targetId: CombatData? = null
        when (action) {
            CombatRunningActions.Companion.CAUSED_DAMAGE -> targetId = this.combatData.find { it.idPlayer != idAI && it.hp > 0}
            CombatRunningActions.Companion.DEFENSE_RECEIVED -> targetId = this.combatData.find { it.idPlayer == idAI && it.hp > 0}
        }
        return  targetId
    }

    override fun performAIAction(playing: CombatData, action: String): List<CombatData> {
        val target = getAITarget(playing.idPlayer, action)
        val damage = playing.attack - (target?.defense ?: 0)
        val isAttackReceived = action == CombatRunningActions.Companion.CAUSED_DAMAGE
        target?.defense -= if(isAttackReceived) target.defense else 0
        target?.hp = if(action == CombatRunningActions.Companion.CAUSED_DAMAGE) target.hp - damage else target.hp
        target?.defense = if(action == CombatRunningActions.Companion.DEFENSE_RECEIVED) target.defense + 1 else target.defense
        playing.timeToAttack = RunningCombatConstantsManger.Companion.getMaxTimeToAttack()
        return this.combatData
    }

    override fun checkLoser(playerId: String): Boolean {
        return this.combatData.filter { it.idPlayer == playerId }.all { it.hp <= 0 }
    }
}
