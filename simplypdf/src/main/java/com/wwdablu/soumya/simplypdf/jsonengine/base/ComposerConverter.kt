package com.wwdablu.soumya.simplypdf.jsonengine.base

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.jsonengine.Node
import org.json.JSONException
import org.json.JSONObject

internal abstract class ComposerConverter(protected val simplyPdfDocument: SimplyPdfDocument) {

    abstract fun generate(composeJsonObject: JSONObject)
    abstract fun getTypeHandler() : String

    fun getProperties(compose: JSONObject): String? {
        return if (compose.has(Node.TYPE_PROPERTIES)) compose.getJSONObject(Node.TYPE_PROPERTIES)
            .toString() else null
    }
}