package com.jatec.creatures.application.exceptions

import com.jatec.creatures.domain.exceptions.ClientException

class ClientExceptionChecker {

    companion object {
        fun isClientException(e: Exception): Boolean {
            return e is ClientException
        }
    }
}