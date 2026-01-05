package com.jatec

import com.jatec.combats.application.dtos.PlayerAction
import com.jatec.combats.application.service.CombatsService
import com.jatec.combats.application.dtos.MessageCombatRunning
import com.jatec.combats.application.exceptions.CombatNotExistingException
import com.jatec.combats.infrastructure.rules.CombatManagerKtor
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.application.rules.CombatRunningActions
import com.jatec.combats.application.rules.CombatRunningStatus
import com.jatec.combats.infrastructure.controller.CombatsController
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.application.dto.StandardResponse
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import kotlin.getValue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

fun Application.configureSockets() {
    val combatsService by inject<CombatsService>()
    val combatsController = CombatsController(combatsService = combatsService)
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = 3600.seconds
        timeout = 3600.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/ws/combat/{id}/{playerId}") {
            val combatId = call.parameters["id"]
            val playerId = call.parameters["playerId"]
            if (combatId == null || playerId == null)  {
                send(
                    Json.encodeToString(
                        StandardResponse<String?>(
                            error = true,
                            code = ResponseCodeManager.CLIENT_ERROR ,
                            msg = "The id $combatId is not valid",
                            data = null
                        )
                    )
                )
            }
            else {
                var data: CombatCreatedData?
                try {
                    data = combatsController.combatsService.findCombatCreatedData(combatId)
                } catch (e: CombatNotExistingException) {
                    log.info(e.message)
                    send(
                        Json.encodeToString(
                            StandardResponse<String?>(
                                error = true,
                                code = ResponseCodeManager.CLIENT_ERROR ,
                                msg = "The id $combatId is not valid",
                                data = null
                            )
                        )
                    )
                    close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
                    return@webSocket
                }
                val otherPlayerId = if (data.combat.idPlayer1 == playerId) data.combat.idPlayer2 else data.combat.idPlayer1
                var playerSocketMap: HashMap<String, DefaultWebSocketSession>? =
                    CombatManagerKtor.runningCombats.get(playerId)
                if (playerSocketMap == null) {
                    playerSocketMap = HashMap<String, DefaultWebSocketSession>()
                }
                // keep this in case you want player vs player combat
                playerSocketMap.set(playerId, this)
                CombatManagerKtor.runningCombats.set(combatId, playerSocketMap)
                send(
                    Json.encodeToString(
                        StandardResponse<MessageCombatRunning>(
                            error = false,
                            code = ResponseCodeManager.OK ,
                            msg = null,
                            data = MessageCombatRunning(
                                action = CombatRunningActions.WAIT,
                                status = CombatRunningStatus.RUNNING,
                                combatData = data.combatData,
                                next = null
                            )
                        )
                    )
                )

                val combatManagerKtor = CombatManagerKtor(
                    initCombat = data.combat,
                    initCombatData = data.combatData
                )
                // combat loop
                while(true) {
                    val updatedData = combatManagerKtor.updateLoop(TimeSource.Monotonic.markNow())
                    if (combatManagerKtor.checkLoser(playerId)){
                        send(
                            Json.encodeToString(
                                StandardResponse<MessageCombatRunning>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = "Looser!!!",
                                    data = MessageCombatRunning(
                                        action = CombatRunningActions.WAIT,
                                        status = CombatRunningStatus.FINISHED,
                                        combatData = updatedData,
                                        next = null
                                    )
                                )
                            )
                        )
                        break
                    }
                    if (combatManagerKtor.checkLoser(otherPlayerId)){
                        send(
                            Json.encodeToString(
                                StandardResponse<MessageCombatRunning>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = "Winner!!!",
                                    data = MessageCombatRunning(
                                        action = CombatRunningActions.WAIT,
                                        status = CombatRunningStatus.FINISHED,
                                        combatData = updatedData,
                                        next = null
                                    )
                                )
                            )
                        )
                        break
                    }
                    log.info("No winner now")
                    val nextToPlay = combatManagerKtor.getNextCreatureToPlayOrNull()
                    if (nextToPlay == null) {
                        send(
                            Json.encodeToString(
                                StandardResponse<MessageCombatRunning>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessageCombatRunning(
                                        action = CombatRunningActions.WAIT,
                                        status = CombatRunningStatus.RUNNING,
                                        combatData = updatedData,
                                        next = null
                                    )
                                )
                            )
                        )
                        continue
                    }
                    if (nextToPlay.idPlayer == playerId) {
                        send(
                            Json.encodeToString(
                                StandardResponse<MessageCombatRunning>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessageCombatRunning(
                                        action = CombatRunningActions.PLAY,
                                        status = CombatRunningStatus.RUNNING,
                                        combatData = updatedData,
                                        next = nextToPlay.id
                                    )
                                )
                            )
                        )
                        try {
                            val frame = incoming.receive()
                            val jsonString = frame.data.decodeToString()
                            val action =
                                Json.decodeFromString<PlayerAction>(jsonString)
                            if (action.sourceId == nextToPlay.id) {
                                val afterActionData = combatManagerKtor.performAction(action, nextToPlay)
                                send(
                                    Json.encodeToString(
                                        StandardResponse<MessageCombatRunning>(
                                            error = false,
                                            code = ResponseCodeManager.OK,
                                            msg = null,
                                            data = MessageCombatRunning(
                                                action = CombatRunningActions.PLAY,
                                                status = CombatRunningStatus.RUNNING,
                                                combatData = afterActionData,
                                                next = nextToPlay.id
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        catch (_: ClosedReceiveChannelException) {
                            break
                        }
                        catch (e: Exception) {
                            log.error(e.message)
                            send(
                                Json.encodeToString(
                                    StandardResponse<MessageCombatRunning>(
                                        error = true,
                                        code = ResponseCodeManager.CLIENT_ERROR,
                                        msg = "Invalid action request",
                                        data = MessageCombatRunning(
                                            action = CombatRunningActions.WAIT,
                                            status = CombatRunningStatus.RUNNING,
                                            combatData = updatedData,
                                            next = nextToPlay.id
                                        )
                                    )
                                )
                            )
                        }
                    }
                    // TODO: check this value to implement player vs player combat
                    else {//(data.isVsAi) {
                        val action = combatManagerKtor.getAIAction()
                        log.info("ai action: $action")
                        val dataAfterAiMovement =
                            combatManagerKtor
                                .performAIAction(
                                    playing = nextToPlay,
                                    action = action
                                )
                        send(
                            Json.encodeToString(
                                StandardResponse<MessageCombatRunning>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessageCombatRunning(
                                        action = action,
                                        status = CombatRunningStatus.RUNNING,
                                        combatData = dataAfterAiMovement,
                                        next = nextToPlay.id
                                    )
                                )
                            )
                        )
                    }
                }
                playerSocketMap.remove(playerId)
            }
            log.info("closing websocket")
            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }
        log.info("out socket")
    }
}