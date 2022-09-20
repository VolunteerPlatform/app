package com.app.vate.api.model

data class ServerResponse<T>(
    val statusCode: Int,
    val message: String,
    val result: T
)
