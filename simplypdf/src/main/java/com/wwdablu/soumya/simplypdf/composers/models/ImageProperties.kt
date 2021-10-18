package com.wwdablu.soumya.simplypdf.composers.models

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ImageComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class ImageProperties : UnitComposer.Properties() {

    @SerializedName("source")
    @kotlin.jvm.JvmField
    var source: String? = null

    @SerializedName("format")
    @kotlin.jvm.JvmField
    var format: String? = null

    @kotlin.jvm.JvmField
    var alignment: Composer.Alignment = Composer.Alignment.START

    override val propId = ImageComposer::class.java.name + "Properties"
}