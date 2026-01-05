package com.jatec.players.infrastructure.response

import com.jatec.players.application.dtos.MessagePlayerCreatures
import com.jatec.players.application.dtos.MessagePlayers


class PlayersResponseHandlerManager {
    val playersListResponseHandlerMap = HashMap<String, PlayersResponseHandler<MessagePlayers>>()
    val playerCreaturesListResponseHandlerMap = HashMap<String, PlayersResponseHandler<MessagePlayerCreatures>>()
    val playersCreateOneResponseHandlerMap = HashMap<String, PlayersResponseHandler<Any?>>()
}