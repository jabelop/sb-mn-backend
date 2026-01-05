package com.jatec.combats.application.exceptions

import com.jatec.combats.domain.exceptions.ClientException

class ClientExceptionChecker {

    companion object {
        fun isClientException(e: Exception): Boolean {
            return e is ClientException
        }
    }
}