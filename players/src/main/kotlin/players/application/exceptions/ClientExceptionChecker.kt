package com.jatec.players.application.exceptions

import com.jatec.players.domain.exceptions.ClientException

class ClientExceptionChecker {

    companion object {
        fun isClientException(e: Exception): Boolean {
            return e is ClientException
        }
    }
}