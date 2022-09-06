package com.app.vate.api.model

data class ServerResponse<T>(
    val responseCode: Int,
    val message: String,
    val result: T
)
