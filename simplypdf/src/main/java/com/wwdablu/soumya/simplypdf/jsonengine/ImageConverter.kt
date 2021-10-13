package com.wwdablu.soumya.simplypdf.jsonengine

import android.content.Context
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ImageComposer
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties
import org.json.JSONObject

internal class ImageConverter(private val context: Context) : BaseConverter() {

    @Throws(Exception::class)
    public override fun generate(composer: Composer, compose: JSONObject) {
        if (composer !is ImageComposer) {
            return
        }

        val imageProperties = getProperties(compose)
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(compose.getString(Node.COMPOSER_IMAGE_URL))
            .submit()
            .get()
        composer.drawBitmap(
            bitmap,
            if (TextUtils.isEmpty(imageProperties)) null else Gson().fromJson(
                imageProperties,
                ImageProperties::class.java
            )
        )
        bitmap.recycle()
    }
}