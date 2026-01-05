package com.jatec.players.infrastructure.response

import com.jatec.shared.application.dto.StandardResponse
import io.ktor.server.routing.RoutingCall
import java.util.concurrent.CompletableFuture

data class PlayersResponseHandler<T>(val response: CompletableFuture<Any?>, val handler: RoutingCall, var responseEnvelope: StandardResponse<T>?)