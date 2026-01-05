package com.jatec.players.application.exceptions

import com.jatec.players.domain.exceptions.ClientException

class BadRequestException(override val message: String): ClientException(message)