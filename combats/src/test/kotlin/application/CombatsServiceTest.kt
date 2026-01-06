package com.jatec.application

import com.jatec.combats.application.dtos.MessageCombatCreatedData
import com.jatec.combats.application.exceptions.CombatNotExistingException
import com.jatec.combats.application.service.CombatsService
import com.jatec.combats.domain.exceptions.InvalidUUID
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.options.CombatDataOptions
import com.jatec.combats.domain.options.CreatePlayerCombatOptions
import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.domain.repository.CombatsRepository
import com.jatec.repository.CombatsRepositoryTest
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


val combatsModule = module {
    singleOf(::CombatsRepositoryTest) bind CombatsRepository::class
    singleOf(::CombatsService)
}

class CreaturesServiceTest {
    var combatsServiceTest: CombatsService? = null

    @Test
    fun testFindAllCombatsNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(combatsModule)
            }
            application {
                val creaturesService by inject<CombatsService>()
                combatsServiceTest = creaturesService

                val expected = listOf<Combat>(
                    Combat(
                        id = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer1 = "88db3246-9387-4d04-a27f-fab107c33800",
                        idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                        winner = null,
                        ip = "0.0.0.0",
                        port = 6587,
                        startedAt = "2025-12-17T21:30:23",
                        updatedAt = "2025-12-17T23:15:23",
                        finishedAt = null
                    ),
                    Combat(
                        id = "05fb32c6-9387-4744-a27f-fab107c33883",
                        idPlayer1 = "88db3246-9387-4104-a23f-fab107c33800",
                        idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                        winner = "88db3246-9387-4104-a23f-fab107c33800",
                        ip = "0.0.0.1",
                        port = 6587,
                        startedAt = "2025-12-18T01:30:23",
                        updatedAt = "2025-12-18T12:15:23",
                        finishedAt = 1767724807040,
                    )
                )
                val combats = combatsServiceTest!!.find(
                    WhereOptions(
                        playerId = null,
                        combatId = null
                    )
                    
                )
                assertContentEquals(expected, combats)
            }
        }
    }
    @Test
    fun testFindAllCombatsIdCombat2Set() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            val expected = listOf<Combat>(
                Combat(
                    id = "05fb32c6-9387-4744-a27f-fab107c33883",
                    idPlayer1 = "88db3246-9387-4104-a23f-fab107c33800",
                    idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                    winner = "88db3246-9387-4104-a23f-fab107c33800",
                    ip = "0.0.0.1",
                    port = 6587,
                    startedAt = "2025-12-18T01:30:23",
                    updatedAt = "2025-12-18T12:15:23",
                    finishedAt = 1767724807040,
                )
            )
            val combats = combatsServiceTest!!.find(
                WhereOptions(
                    playerId = null,
                    combatId = "05fb32c6-9387-4744-a27f-fab107c33883",
                )
            )
            assertContentEquals(expected, combats)
        }
    }

    @Test
    fun testFindAllCombatsPlayerId1FirstCombatSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            val expected = listOf<Combat>(
                Combat(
                    id = "05fb3246-9387-4d04-a27f-fab107c33883",
                    idPlayer1 = "88db3246-9387-4d04-a27f-fab107c33800",
                    idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                    winner = null,
                    ip = "0.0.0.0",
                    port = 6587,
                    startedAt = "2025-12-17T21:30:23",
                    updatedAt = "2025-12-17T23:15:23",
                    finishedAt = null
                ),
            )
            val combats = combatsServiceTest!!.find(
                WhereOptions(
                    playerId = "88db3246-9387-4d04-a27f-fab107c33800",
                    combatId = null,
                )
            )
            assertContentEquals(expected, combats)
        }
    }


    @Test
    fun testFindAllCreaturesThrowBadUUIDOnBadCombatIdException() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.find(
                        WhereOptions(
                            playerId = "",
                           combatId = null,
                        )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testFindAllCreaturesThrowBadUUIDOnBadPlayerIdException() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.find(
                    WhereOptions(
                        playerId = "0987-4d04-a27f-fab107c33883",
                        combatId = null,
                    )

                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    // Find all player combats
    @Test
    fun testFindAllPlayerCombatsNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(combatsModule)
            }
            application {
                val creaturesService by inject<CombatsService>()
                combatsServiceTest = creaturesService

                val expected = listOf<CombatData>(
                    CombatData(
                        idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c338aa",
                        name = "Creature 1",
                        creatureClass = "warrior",
                        level = 1,
                        xp = 123,
                        hp = 130,
                        speed = 26,
                        attack = 32,
                        defense = 2,
                        timeToAttack = 24
                    ),
                    CombatData(
                        idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer = "88dba046-93b7-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                        name = "Creature 3",
                        creatureClass = "enchanter",
                        level = 1,
                        xp = 133,
                        hp = 132,
                        speed = 34,
                        attack = 26,
                        defense = 2,
                        timeToAttack = 18
                    ),
                    CombatData(
                        idCombat = "05fb32c6-9387-4744-a27f-fab107c33883",
                        idPlayer = "88dba046-93b7-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c338aa",
                        name = "Creature 1",
                        creatureClass = "warrior",
                        level = 3,
                        xp = 312,
                        hp = 0,
                        speed = 37,
                        attack = 30,
                        defense = 3,
                        timeToAttack = 32
                    ),
                    CombatData(
                        idCombat = "05fb32c6-9387-4744-a27f-fab107c33883",
                        idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                        name = "Creature 1",
                        creatureClass = "enchanter",
                        level = 3,
                        xp = 316,
                        hp = 170,
                        speed = 43,
                        attack = 27,
                        defense = 3,
                        timeToAttack = 14
                    )
                )
                val playerCombats = combatsServiceTest!!.findPlayerCombats(
                    WhereOptions(
                        playerId = null,
                        combatId = null
                    )
                )
                assertContentEquals(expected, playerCombats)
            }
        }
    }

    @Test
    fun testFindAllPlayerCombatsIdPlayerFirstAndLastSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            val expected = listOf<CombatData>(
                CombatData(
                    idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c338aa",
                    name = "Creature 1",
                    creatureClass = "warrior",
                    level = 1,
                    xp = 123,
                    hp = 130,
                    speed = 26,
                    attack = 32,
                    defense = 2,
                    timeToAttack = 24
                ),
                CombatData(
                    idCombat = "05fb32c6-9387-4744-a27f-fab107c33883",
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                    name = "Creature 1",
                    creatureClass = "enchanter",
                    level = 3,
                    xp = 316,
                    hp = 170,
                    speed = 43,
                    attack = 27,
                    defense = 3,
                    timeToAttack = 14
                )
            )
            val playerCreatures = combatsServiceTest!!.findPlayerCombats(
                WhereOptions(
                    playerId = "88db3246-9387-4d04-a27f-fab107c33800",
                    combatId = null
                )

            )
            assertContentEquals(expected, playerCreatures)
        }
    }

    @Test
    fun testFindAllPlayerCombatsThrowBadUUIDOnBadCombatIdException() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.findPlayerCombats(
                    WhereOptions(
                        playerId = null,
                        combatId = "77hha7777713hh-32737-324"
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testFindAllPlayerCombatsThrowBadUUIDOnBadPlayerIdException() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.findPlayerCombats(
                    WhereOptions(
                        playerId = "0987-4d04-a27f-fab107c33883",
                        combatId = null
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }


    // Create one
    @Test
    fun testCreateOneCombat() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService
            val combat = Combat(
                id = "444b3246-9387-4d04-a27f-fab107c33800",
                idPlayer1 = "88db3246-9387-4d04-a27f-fab107c33800",
                idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                winner = null,
                ip = "0.0.0.0",
                port = 6654,
                startedAt = "2025-12-18T01:30:23",
                updatedAt = "2025-12-18T12:15:23",
                finishedAt = null
            )
            val combatData = listOf<CombatData>(
                CombatData(
                    idCombat = "444b3246-9387-4d04-a27f-fab107c33800",
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                    name = "Creature 1",
                    creatureClass = "enchanter",
                    level = 3,
                    xp = 316,
                    hp = 170,
                    speed = 43,
                    attack = 27,
                    defense = 0,
                    timeToAttack = 50
                ),
                CombatData(
                    idCombat = "444b3246-9387-4d04-a27f-fab107c33800",
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                    name = "Creature 1",
                    creatureClass = "enchanter",
                    level = 3,
                    xp = 316,
                    hp = 170,
                    speed = 43,
                    attack = 27,
                    defense = 0,
                    timeToAttack = 50
                )
            )
            val combatDataOptions = listOf<CombatDataOptions>(
                CombatDataOptions(
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                    name = "Creature 1",
                    creatureClass = "enchanter",
                    level = 3,
                    xp = 316,
                    hp = 170,
                    speed = 43,
                    attack = 27,
                ),
                CombatDataOptions(
                    idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                    id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                    name = "Creature 1",
                    creatureClass = "enchanter",
                    level = 3,
                    xp = 316,
                    hp = 170,
                    speed = 43,
                    attack = 27,
                )
            )
            val expected = MessageCombatCreatedData(
                combatCreatedData = CombatCreatedData(
                    combat = combat,
                    combatData = combatData
                ),
                isVsAi = true,
                playerId = "88db3246-9387-4d04-a27f-fab107c33800"
            )
            val result = combatsServiceTest!!.create(
                CreatePlayerCombatOptions(
                    player1Id = "88db3246-9387-4d04-a27f-fab107c33800",
                    player2Id = "88dba046-93b7-4d04-a27f-fab107c33800",
                    combatData = combatDataOptions,
                    isVsAi = true
                )
            )
            assertEquals(expected, result)
        }
    }

    @Test
    fun testCreateOneCombatThrowInvalidUUIDOnNoPlayer1IdSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.create(
                    CreatePlayerCombatOptions(
                        player1Id = "",
                        player2Id = "88dba046-93b7-4d04-a27f-fab107c33800",
                        combatData = emptyList(),
                        isVsAi = true
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testCreateOneCombatThrowInvalidUUIDOnNoPlayer2IdSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.create(
                    CreatePlayerCombatOptions(
                        player1Id = "88dba046-93b7-4d04-a27f-fab107c33800",
                        player2Id = "",
                        combatData = emptyList(),
                        isVsAi = true
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    //TODO: implement the findCombatCreatedData tests

    // Find combat created data
    @Test
    fun testFindAllCombatCreatedDataNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(combatsModule)
            }
            application {
                val creaturesService by inject<CombatsService>()
                combatsServiceTest = creaturesService

                val expected = CombatCreatedData(
                    combat = Combat(
                        id = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer1 = "88db3246-9387-4d04-a27f-fab107c33800",
                        idPlayer2 = "88dba046-93b7-4d04-a27f-fab107c33800",
                        winner = null,
                        ip = "0.0.0.0",
                        port = 6587,
                        startedAt = "2025-12-17T21:30:23",
                        updatedAt = "2025-12-17T23:15:23",
                        finishedAt = null
                    ),
                    combatData = listOf<CombatData>(
                    CombatData(
                        idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer = "88db3246-9387-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c338aa",
                        name = "Creature 1",
                        creatureClass = "warrior",
                        level = 1,
                        xp = 123,
                        hp = 130,
                        speed = 26,
                        attack = 32,
                        defense = 2,
                        timeToAttack = 24
                    ),
                    CombatData(
                        idCombat = "05fb3246-9387-4d04-a27f-fab107c33883",
                        idPlayer = "88dba046-93b7-4d04-a27f-fab107c33800",
                        id = "77db3246-9387-4d04-a27f-5ab123c228aa",
                        name = "Creature 3",
                        creatureClass = "enchanter",
                        level = 1,
                        xp = 133,
                        hp = 132,
                        speed = 34,
                        attack = 26,
                        defense = 2,
                        timeToAttack = 18
                    )
                    )
                )
                val combatCreatedData = combatsServiceTest!!.findCombatCreatedData(
                    "05fb3246-9387-4d04-a27f-fab107c33883"
                )
                assertEquals(expected, combatCreatedData)
            }
        }
    }

    @Test
    fun testFindAllCombatCreatedDataThrowCombatNotExistingExceptionOnNoExistingIdSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.findCombatCreatedData("88dba046-93b7-4d04-a27f-fab107c33800")
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is CombatNotExistingException -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testFindAllCombatCreatedDataThrowBadUUIDExceptionOnNoIdSet() = testApplication {
        install(Koin) {
            modules(combatsModule)
        }
        application {
            val creaturesService by inject<CombatsService>()
            combatsServiceTest = creaturesService

            try {
                combatsServiceTest!!.findCombatCreatedData("")
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }
}
