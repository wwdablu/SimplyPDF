package com.wwdablu.soumya.simplypdf.jsonengine.base

import com.wwdablu.soumya.simplypdf.jsonengine.Node
import org.json.JSONObject

internal abstract class PageConverter {

    abstract fun generate(compose: JSONObject)
    abstract fun getTypeHandler() : String

    fun getProperties(compose: JSONObject): String? {
        return if (compose.has(Node.TYPE_PROPERTIES)) compose.getJSONObject(Node.TYPE_PROPERTIES)
            .toString() else null
    }
}