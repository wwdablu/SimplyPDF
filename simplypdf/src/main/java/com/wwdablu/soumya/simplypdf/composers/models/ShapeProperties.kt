package com.wwdablu.soumya.simplypdf.composers.models

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class ShapeProperties : UnitComposer.Properties() {

    @kotlin.jvm.JvmField
    @SerializedName("linecolor")
    var lineColor = "#000000"

    @kotlin.jvm.JvmField
    @SerializedName("linewidth")
    var lineWidth = 1

    @kotlin.jvm.JvmField
    @SerializedName("shouldfill")
    var shouldFill = false

    @kotlin.jvm.JvmField
    var alignment: Composer.Alignment = Composer.Alignment.START

    override val propId = ShapeComposer::class.java.name + "Properties"
}