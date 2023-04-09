package com.example.aut.data_classes

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("identificator")
    var identificator: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("timeZone")
    var timeZone: String? = null,
    @SerializedName("captchaName")
    var captchaName: String? = null,
    @SerializedName("captchaToken")
    var captchaToken: String? = null
)
