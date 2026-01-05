package com.jatec.shared.infrastructure.communication

import com.jatec.shared.application.communication.ResponseCodeManager

class ResponseCodeManagerHttp<HttpStatusCode>: ResponseCodeManager<HttpStatusCode>() {
    @Suppress("UNCHECKED_CAST")
    override fun getApplicationErrorCode(code: Int): HttpStatusCode {
        return when(code) {
            NOT_FOUND -> io.ktor.http.HttpStatusCode.Companion.NotFound
            CLIENT_ERROR -> io.ktor.http.HttpStatusCode.Companion.BadRequest
            SERVER_ERROR -> io.ktor.http.HttpStatusCode.Companion.InternalServerError
            else -> io.ktor.http.HttpStatusCode.Companion.OK
        } as HttpStatusCode
    }

}