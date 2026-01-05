package com.jatec.combats.application.dto.builders

import com.jatec.combats.application.dto.MessageCombatCreator
import com.jatec.combats.application.dto.MessagePlayerCombatCreator
import com.jatec.combats.domain.options.CombatDataOptions
import com.jatec.combats.domain.options.CreatePlayerCombatOptions

class MessageCombatCreatorBuilder {

    companion object {
        fun buildMessageCombatCreatorFromMessagePlayerCombatCreatorAndCombatData(
            messageCombatCreator: MessagePlayerCombatCreator,
            combatData: List<CombatDataOptions>
        ): MessageCombatCreator {
            return MessageCombatCreator(
                options = CreatePlayerCombatOptions(
                    player1Id = messageCombatCreator.player1Id,
                    player2Id = messageCombatCreator.player2Id,
                    combatData = combatData,
                    isVsAi = messageCombatCreator.isVsAi
                )
            )
        }
    }
}