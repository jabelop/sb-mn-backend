package com.jatec.combats.application.rules

import com.jatec.combats.domain.constants.CHOOSE_DEFENSE_THRESHOLD_AI
import com.jatec.combats.domain.constants.TIME_TO_ATTACK

class RunningCombatConstantsManger {

    companion object {
        fun getMaxTimeToAttack(): Int {
            return TIME_TO_ATTACK
        }

        fun getChooseDefenseThresholdAi(): Double {
            return CHOOSE_DEFENSE_THRESHOLD_AI
        }
    }
}