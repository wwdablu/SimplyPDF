package com.wwdablu.soumya.simplypdf.composers.models

import com.google.gson.annotations.SerializedName
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ImageComposer
import com.wwdablu.soumya.simplypdf.composers.UnitComposer

class ImageProperties : UnitComposer.Properties() {
    @SerializedName("imageurl")
    var imageUrl: String? = null
    @kotlin.jvm.JvmField
    var alignment: Composer.Alignment = Composer.Alignment.LEFT
    override val propId: String
        get() = ImageComposer::class.java.name + "Properties"

}