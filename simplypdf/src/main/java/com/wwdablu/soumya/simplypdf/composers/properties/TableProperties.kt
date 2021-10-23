package com.wwdablu.soumya.simplypdf.composers.properties

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class TableProperties : UnitComposer.Properties() {

    @JvmField
    @SerializedName("width")
    var borderWidth = 0

    @JvmField
    @SerializedName("color")
    var borderColor: String? = null

    @JvmField
    @SerializedName("drawborder")
    var drawBorder: Boolean = true

    override val propId = TableComposer::class.java.name + "Properties"
}