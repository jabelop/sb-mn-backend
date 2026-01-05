package com.jatec.shared.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandardResponse<E>(val error: Boolean, val code: Int?, val msg: String?, val data: E?)