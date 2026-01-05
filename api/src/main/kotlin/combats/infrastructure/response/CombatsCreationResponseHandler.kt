package com.jatec.combats.infrastructure.response

import com.jatec.shared.application.dto.StandardResponse
import io.ktor.server.routing.RoutingCall
import java.util.concurrent.CompletableFuture

class CombatsCreationResponseHandler<T>(val response: CompletableFuture<Any?>, val handler: RoutingCall, var responseEnvelope: StandardResponse<T>?)