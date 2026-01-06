package com.jatec.combats.application.rules

import com.jatec.combats.application.dtos.PlayerAction
import com.jatec.combats.domain.model.CombatData
import kotlin.time.TimeSource.Monotonic.ValueTimeMark

interface CombatManager {

    /**
     * updates the list of combat data in combat with the elapsed time
     *
     * @param startPeriod {ValueTimeMark} the mark time object to calculated the elapsed time
     *
     * @return List<CombatData> the list of combat data updated
     */
    fun updateLoop(startPeriod: ValueTimeMark): List<CombatData>

    /**
     * get the next combat data representing a creature in combat to play or null if there is no one
     *
     * @return CombatData? the next combat data representing a creature in combat to play or null if there is no one
     */
    fun getNextCreatureToPlayOrNull(): CombatData?

    /**
     * perform an action over a combat data representing a creature in combat.
     *
     * @param action {PlayerAction} the player action to be performed
     * @param source {CombatData} the combat data representing the creature in combat which performs the action
     *
     * @return List<CombatData> the list of the combat data updated after the action is performed
     */
    fun performAction(action: PlayerAction, source: CombatData): List<CombatData>

    /**
     * get the next ai action to be performed
     *
     */
    fun getAIAction(): String

    /**
     * get the next ai target
     *
     * @param idAI {String} the id of the ai player in DB
     * @param action {String} the action to be performed by the ai player
     *
     */
    fun getAITarget(idAI: String, action: String): CombatData?

    /**
     * perform an ai action over a combat data representing a creature in combat.
     *
     * @param playing {CombatData} the ai combat data representing the creature in combat which performs the action
     * @param action {String} the ai action to be performed
     *
     * @return List<CombatData> the list of the combat data updated after the ai action is performed
     */
    fun performAIAction(playing: CombatData, action: String): List<CombatData>

    /**
     * check if a player has lost the combat
     *
     * @param playerId {String} the player id to be checked
     *
     * @return true if the player has lost the combat, false otherwise
     */
    fun checkLoser(playerId: String): Boolean

    /**
     * set the received player as the winner of the combat
     *
     * @param playerId {String} the player id who won the combat
     *
     */
    fun setCombatWinner(playerId: String)
}