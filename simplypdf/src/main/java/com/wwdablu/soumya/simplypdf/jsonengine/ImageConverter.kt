package com.wwdablu.soumya.simplypdf.jsonengine

import android.content.Context
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class ImageConverter(private val context: Context, simplyPdfDocument: SimplyPdfDocument)
    : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {

        val imageProperties = getProperties(composeJsonObject)
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(composeJsonObject.getString(Node.COMPOSER_IMAGE_SOURCE))
            .submit()
            .get()
        simplyPdfDocument.image.drawBitmap(
            bitmap,
            if (TextUtils.isEmpty(imageProperties)) ImageProperties() else Gson().fromJson(
                imageProperties,
                ImageProperties::class.java
            )
        )
        bitmap.recycle()
    }

    override fun getTypeHandler(): String = "image"
}