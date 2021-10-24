package com.wwdablu.soumya.simplypdf.jsonengine.setup

import com.google.gson.annotations.SerializedName

data class Setup(

    @SerializedName("type")
    val type: String,

    @SerializedName("margin")
    val margin: Margin?,

    @SerializedName("backgroundcolor")
    val backgroundColor: String?
)
