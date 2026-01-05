package com.jatec.combats.application.dto.builders

import com.jatec.combats.application.dto.MessageFinderCreatures
import com.jatec.combats.application.dto.MessagePlayerCombatCreator
import com.jatec.shared.domain.options.creatures.WhereOptions

class MessageFinderCreaturesBuilder {

    companion object {
        fun buildMessageFindCreaturesFromMessagePlayerCombatCreator(
            messagePlayerCombatCreator: MessagePlayerCombatCreator
        ): MessageFinderCreatures {
            return MessageFinderCreatures(
                where = WhereOptions(
                    playerId = messagePlayerCombatCreator.player1Id,
                    enemyId = messagePlayerCombatCreator.player2Id,
                    creatureId = null,
                    name = null
                )
            )
        }
    }
}