package com.wwdablu.soumya.simplypdf.jsonengine.composerconverters

import android.content.Context
import com.bumptech.glide.Glide
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.jsonengine.Node
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class ImageConverter(private val context: Context, simplyPdfDocument: SimplyPdfDocument)
    : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {

        val imageProperties: ImageProperties = getProperties(composeJsonObject, Node.TYPE_PROPERTIES)

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(composeJsonObject.getString(Node.COMPOSER_IMAGE_SOURCE))
            .submit()
            .get()
        simplyPdfDocument.image.drawBitmap(bitmap, imageProperties)
    }

    override fun getTypeHandler(): String = "image"
}