package com.wwdablu.soumya.simplypdf.jsonengine

import android.text.TextUtils
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.TextComposer
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties
import org.json.JSONException
import org.json.JSONObject

internal class TextConverter : BaseConverter() {

    @Throws(JSONException::class)
    public override fun generate(composer: Composer, compose: JSONObject) {

        if (composer !is TextComposer) {
            return
        }

        //Check if properties has been defined
        val textProperties = getProperties(compose)
        composer.write(
            compose.getString(Node.Companion.COMPOSER_TEXT_CONTENT),
            if (TextUtils.isEmpty(textProperties)) null else Gson().fromJson(
                textProperties,
                TextProperties::class.java
            )
        )
    }
}