package com.jatec.shared.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandardResponse<E>(var error: Boolean, var code: Int?, var msg: String?, var data: E?)