package com.wwdablu.soumya.simplypdf.composers.models

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import android.graphics.Typeface
import android.text.TextUtils
import com.wwdablu.soumya.simplypdf.composers.TextComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer
import android.text.Layout

class TextProperties : UnitComposer.Properties() {
    @kotlin.jvm.JvmField
    @SerializedName("color")
    var textColor = "#000000"

    @kotlin.jvm.JvmField
    @SerializedName("size")
    var textSize = 10

    @kotlin.jvm.JvmField
    @SerializedName("isBullet")
    var isBullet: Boolean

    @kotlin.jvm.JvmField
    @SerializedName("bulletSymbol")
    var bulletSymbol: String?

    @kotlin.jvm.JvmField
    @SerializedName("underline")
    var underline: Boolean

    @kotlin.jvm.JvmField
    @SerializedName("strikethrough")
    var strikethrough: Boolean
    @kotlin.jvm.JvmField
    var typeface: Typeface? = null
    @kotlin.jvm.JvmField
    var alignment: Layout.Alignment?
    fun getAlignment(): Layout.Alignment? {
        if (alignment == null) {
            alignment = Layout.Alignment.ALIGN_NORMAL
        }
        return alignment
    }

    fun getBulletSymbol(): String? {
        if (bulletSymbol == null || TextUtils.isEmpty(bulletSymbol)) {
            bulletSymbol = ""
            return ""
        }
        if (bulletSymbol!!.length >= 2) {
            bulletSymbol = bulletSymbol!!.substring(0, 1)
        }
        return bulletSymbol
    }

    override val propId: String
        get() = TextComposer::class.java.name + "Properties"

    init {
        alignment = Layout.Alignment.ALIGN_NORMAL
        isBullet = false
        bulletSymbol = ""
        underline = false
        strikethrough = false
    }
}