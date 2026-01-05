package com.jatec.creatures.application.exceptions

import com.jatec.creatures.domain.exceptions.ClientException

class BadRequestException(override val message: String): ClientException(message)