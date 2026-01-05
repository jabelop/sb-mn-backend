package com.jatec.combats.infrastructure.response

import com.jatec.combats.application.dto.MessageCombats
import com.jatec.combats.application.dto.MessagePlayerCombatCreatures
import com.jatec.combats.application.dto.MessagePlayerCombatData
import com.jatec.shared.infrastructure.response.ResponseHandler

class CombatsResponseHandlerManager {
    val combatsListResponseHandlerMap = HashMap<String, ResponseHandler>()
    val playerCombatsListResponseHandlerMap = HashMap<String, ResponseHandler>()
    val combatsCreateOneResponseHandlerMap = HashMap<String, CombatsCreationResponseHandler<MessageCombats>>()
    val combatsPlayerCreaturesResponseHandlerMap = HashMap<String, CombatsCreationResponseHandler<MessagePlayerCombatCreatures>>()
    val combatsPlayerCombatDataResponseHandlerMap = HashMap<String, ResponseHandler>()
    val combatsUpdateOneResponseHandlerMap = HashMap<String, ResponseHandler>()
}