package com.jatec

import com.jatec.combats.application.dtos.PlayerAction
import com.jatec.combats.application.service.CombatsService
import com.jatec.combats.application.dtos.MessageCombatRunning
import com.jatec.combats.application.exceptions.CombatNotExistingException
import com.jatec.combats.infrastructure.rules.CombatManagerKtor
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.application.rules.CombatRunningActions
import com.jatec.combats.application.rules.CombatRunningStatus
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.infrastructure.controller.CombatsController
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.application.dto.StandardResponse
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
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
                var combat: Combat?
                try {
                    data = combatsController.combatsService.findCombatCreatedData(combatId)
                    combat = combatsController.combatsService.find(WhereOptions(
                        playerId = null,
                        combatId = combatId
                    )).first()
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
                                combat = combat,
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
                var updatedData = data.combatData
                while(true) {
                    val nextToPlay = combatManagerKtor.getNextCreatureToPlayOrNull()
                    if (nextToPlay == null) {
                        log.info("no next to play")
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
                                        combat = combatManagerKtor.combat,
                                        next = null
                                    )
                                )
                            )
                        )
                    }
                    else if (nextToPlay.idPlayer == playerId) {
                        log.info("next to play player")
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
                                        combat = combatManagerKtor.combat,
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
                                                combat = combatManagerKtor.combat,
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
                                            combat = combatManagerKtor.combat,
                                            next = nextToPlay.id
                                        )
                                    )
                                )
                            )
                        }
                        log.info("performed action")
                        log.info(playerId)
                        continue
                    }
                    // TODO: check this value to implement player vs player combat
                    else {//(data.isVsAi) {
                        log.info("next to play ai")
                        log.info(nextToPlay.idPlayer)
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
                                        combat = combatManagerKtor.combat,
                                        next = nextToPlay.id
                                    )
                                )
                            )
                        )
                        continue
                    }
                    log.info("updating")
                    updatedData = combatManagerKtor.updateLoop(TimeSource.Monotonic.markNow())
                    launch {
                        try {
                            combatsController.combatsService.updatePlayerCombat(
                                combat = combatManagerKtor.combat,
                                combatData = updatedData
                            )
                        } catch (e: Exception) {
                            log.info("Error updating player combat")
                            log.info(e.message)
                            e.printStackTrace()
                        }
                    }
                    if (combatManagerKtor.checkLoser(playerId)){
                        combatManagerKtor.setCombatWinner(otherPlayerId)
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
                                        combat = combatManagerKtor.combat,
                                        next = null
                                    )
                                )
                            )
                        )
                        break
                    }
                    if (combatManagerKtor.checkLoser(otherPlayerId)){
                        combatManagerKtor.setCombatWinner(playerId)
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
                                        combat = combatManagerKtor.combat,
                                        next = null
                                    )
                                )
                            )
                        )
                        break
                    }
                    log.info("No winner now")
                }
                launch {
                    try {
                        combatsController.combatsService.updatePlayerCombat(
                            combat = combatManagerKtor.combat,
                            combatData = combatManagerKtor.combatData
                        )
                    } catch (e: Exception) {
                        log.info("Error updating player combat")
                        log.info(e.message)
                        e.printStackTrace()
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