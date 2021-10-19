package com.wwdablu.soumya.simplypdf.composers.properties

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class TableProperties : UnitComposer.Properties() {

    @kotlin.jvm.JvmField
    @SerializedName("width")
    var borderWidth = 0

    @kotlin.jvm.JvmField
    @SerializedName("color")
    var borderColor: String? = null

    override val propId = TableComposer::class.java.name + "Properties"
}