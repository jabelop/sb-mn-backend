package com.jatec.players.application.exceptions

import com.jatec.players.domain.exceptions.ClientException

class PlayerExistingException(message: String): ClientException(message)