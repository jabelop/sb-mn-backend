package com.jatec.creatures.application.exceptions

import com.jatec.creatures.domain.exceptions.ClientException

class PlayerCreatureExistingException(override val message: String): ClientException(message)