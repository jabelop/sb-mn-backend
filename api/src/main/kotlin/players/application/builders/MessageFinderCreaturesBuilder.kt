package com.jatec.players.application.builders

import com.jatec.players.application.dtos.MessageFinder
import com.jatec.shared.domain.options.creatures.WhereOptions
import com.jatec.shared.application.dto.creatures.MessageFinder as MessageFinderCreatures

class MessageFinderCreaturesBuilder {
   companion object {

       fun buildMessageFinderCreaturesFromMessageFinderPlayer(
           messageFinder: MessageFinder
       ): MessageFinderCreatures  {
           return MessageFinderCreatures(
               where = WhereOptions(
                   playerId = messageFinder.where.playerId,
                   creatureId = messageFinder.where.creatureId,
                   name = messageFinder.where.name,
                   enemyId = null
               )
           )
       }
   }
}