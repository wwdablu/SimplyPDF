package com.wwdablu.soumya.simplypdf.jsonengine.base

import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import org.json.JSONObject

abstract class ComposerConverter(protected val simplyPdfDocument: SimplyPdfDocument) {

    abstract fun generate(composeJsonObject: JSONObject)
    abstract fun getTypeHandler() : String

    inline fun <reified T> getProperties(compose: JSONObject, propertyNodeName: String) : T {
        return Gson().fromJson(compose.getString(propertyNodeName), T::class.java)
    }
}