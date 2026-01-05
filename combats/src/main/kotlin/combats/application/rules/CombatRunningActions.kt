package com.jatec.combats.application.rules

class CombatRunningActions {

    companion object {
        const val PLAY = "PLAY"
        const val WAIT = "WAIT"
        const val RECEIVED_DAMAGE = "RECEIVED_DAMAGE"
        const val CAUSED_DAMAGE = "CAUSED_DAMAGE"
        const val DEFENSE_RECEIVED = "DEFENSE_RECEIVED"
        val actions = listOf(PLAY, WAIT, RECEIVED_DAMAGE, CAUSED_DAMAGE, DEFENSE_RECEIVED)

        fun isValidAction(action: String): Boolean {
            return actions.contains(action)
        }
    }
}