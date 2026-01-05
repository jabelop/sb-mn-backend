package com.jatec.shared.application.communication

abstract class ResponseCodeManager<E> {
    companion object {
        const val NOT_FOUND = 1
        const val CLIENT_ERROR = 2
        const val SERVER_ERROR = 3
        const val unknown = 100

    }

    abstract fun getApplicationErrorCode(code: Int): E

}