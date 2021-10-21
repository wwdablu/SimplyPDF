package com.wwdablu.soumya.simplypdf.jsonengine

import android.text.TextUtils
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class TextConverter(simplyPdfDocument: SimplyPdfDocument) : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {

        //Check if properties has been defined
        val textProperties = getProperties(composeJsonObject)
        simplyPdfDocument.text.write(
            composeJsonObject.getString(Node.COMPOSER_TEXT_CONTENT),
            if (TextUtils.isEmpty(textProperties)) TextProperties() else Gson().fromJson(
                textProperties,
                TextProperties::class.java
            )
        )
    }

    override fun getTypeHandler(): String = "shape"
}