package com.app.vate.model

import java.io.Serializable

data class Address (
    var detailAddress: String,
    var zipcode : String,
    var coordinate : Coordinate
) : Serializable
