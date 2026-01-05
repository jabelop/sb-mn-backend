package com.jatec.combats.domain.exceptions

class SamePlayersException(override val message: String): ClientException(message)