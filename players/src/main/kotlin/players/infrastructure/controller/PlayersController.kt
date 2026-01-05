package com.jatec.players.infrastructure.controller

import com.jatec.players.application.service.PlayersService

data class PlayersController(val playersService: PlayersService)