package com.jatec.shared.application.communication

class ResponseCodeManager {
    companion object {
        const val OK = 0
        const val NOT_FOUND = 1
        const val CLIENT_ERROR = 2
        const val SERVER_ERROR = 3
        const val unknown = 100

    }
}