package com.jatec.combats.application.exceptions

import com.jatec.combats.domain.exceptions.ClientException

class BadRequestException(override val message: String): ClientException(message)