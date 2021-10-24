package com.wwdablu.soumya.simplypdf.composers.properties

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ImageComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class ImageProperties : UnitComposer.Properties() {

    @SerializedName("source")
    @kotlin.jvm.JvmField
    internal var source: String? = null

    @SerializedName("format")
    @kotlin.jvm.JvmField
    internal var format: String? = null

    @kotlin.jvm.JvmField
    var alignment: Composer.Alignment = Composer.Alignment.START

    override val propId = ImageComposer::class.java.name + "Properties"
}