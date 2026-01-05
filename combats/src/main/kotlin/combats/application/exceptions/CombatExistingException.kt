package com.jatec.combats.application.exceptions

import com.jatec.combats.domain.exceptions.ClientException

class CombatExistingException(override val message: String): ClientException(message)