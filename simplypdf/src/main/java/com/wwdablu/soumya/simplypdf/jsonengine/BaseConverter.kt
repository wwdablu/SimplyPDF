package com.wwdablu.soumya.simplypdf.jsonengine

import com.wwdablu.soumya.simplypdf.composers.Composer
import org.json.JSONException
import org.json.JSONObject

internal abstract class BaseConverter {
    @Throws(Exception::class)
    protected abstract fun generate(composer: Composer, compose: JSONObject)

    @Throws(JSONException::class)
    protected fun getProperties(compose: JSONObject): String? {
        return if (compose.has(Node.TYPE_PROPERTIES)) compose.getJSONObject(Node.TYPE_PROPERTIES)
            .toString() else null
    }
}