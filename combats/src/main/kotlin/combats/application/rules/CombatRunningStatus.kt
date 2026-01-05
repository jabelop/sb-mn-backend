package com.jatec.combats.application.rules

class CombatRunningStatus {

    companion object {
        const val RUNNING = "RUNNING"
        const val PAUSED = "PAUSED"
        const val FINISHED = "FINISHED"
        val status = listOf(RUNNING, PAUSED, FINISHED)

        fun isValidStatus(action: String): Boolean {
            return status.contains(action)
        }
    }
}