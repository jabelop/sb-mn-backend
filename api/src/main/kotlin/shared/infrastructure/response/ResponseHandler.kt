package com.jatec.shared.infrastructure.response

import io.ktor.server.routing.RoutingCall
import java.util.concurrent.CompletableFuture

data class ResponseHandler (val response: CompletableFuture<Any?>, val handler: RoutingCall)