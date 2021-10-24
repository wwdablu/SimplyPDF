package com.wwdablu.soumya.simplypdf.composers.properties

import android.graphics.Typeface
import android.text.Layout
import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.TextComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class TextProperties : UnitComposer.Properties() {

    @kotlin.jvm.JvmField
    @SerializedName("color")
    var textColor = "#000000"

    @kotlin.jvm.JvmField
    @SerializedName("size")
    var textSize = 10

    @kotlin.jvm.JvmField
    @SerializedName("isBullet")
    var isBullet: Boolean = false

    @kotlin.jvm.JvmField
    @SerializedName("bulletSymbol")
    var bulletSymbol: String? = ""

    @kotlin.jvm.JvmField
    @SerializedName("underline")
    var underline: Boolean = false

    @kotlin.jvm.JvmField
    @SerializedName("strikethrough")
    var strikethrough: Boolean = false

    @kotlin.jvm.JvmField
    var typeface: Typeface? = null

    @kotlin.jvm.JvmField
    var alignment: Layout.Alignment? = Layout.Alignment.ALIGN_NORMAL

    fun getAlignment(): Layout.Alignment? {
        if (alignment == null) {
            alignment = Layout.Alignment.ALIGN_NORMAL
        }
        return alignment
    }

    fun getBulletSymbol(): String {

        if ((bulletSymbol?.length ?: 1) >= 2) {
            bulletSymbol = bulletSymbol?.substring(0, 1)
        }
        return bulletSymbol ?: ""
    }

    override val propId = TextComposer::class.java.name + "Properties"
}