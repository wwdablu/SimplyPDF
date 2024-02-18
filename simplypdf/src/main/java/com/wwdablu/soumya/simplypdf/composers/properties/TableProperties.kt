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
    var borderColor: String = "#000000"

    @JvmField
    @SerializedName("drawborder")
    var drawBorder: Boolean = true

    @JvmField
    @SerializedName("borderstyle")
    var borderStyle: Int = BORDER_ALL

    @JvmField
    @SerializedName("align")
    var align: Int = ALIGN_LEFT

    override val propId = TableComposer::class.java.name + "Properties"

    companion object {
        val BORDER_OUTER = 1
        val BORDER_INNER = 1 shl 1
        val BORDER_HORIZONTAL = 1 shl 2
        val BORDER_VERTICAL = 1 shl 3
        val BORDER_ALL = 1 shl 7

        val ALIGN_LEFT = 1
        val ALIGN_CENTER = 1 shl 1
    }
}