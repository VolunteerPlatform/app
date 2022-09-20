package com.app.vate.model

import java.io.Serializable

data class VolOrgan (
    var id : Long,
    var name : String,
    var manager : String,
    var organPhoneNumber : String,
    var address : Address
) : Serializable