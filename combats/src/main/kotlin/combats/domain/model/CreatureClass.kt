package com.jatec.combats.domain.model

class CreatureClass {
    companion object {
        const val DEFENDER = "defender"
        const val ENCHANTER = "enchanter"
        const val WARRIOR = "warrior"
        val classes = listOf(DEFENDER, ENCHANTER, WARRIOR)

        fun isValidClass(creatureClass: String): Boolean {
            return classes.contains(creatureClass)
        }
    }
}