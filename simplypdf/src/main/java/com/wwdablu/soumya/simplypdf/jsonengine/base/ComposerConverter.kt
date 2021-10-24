package com.wwdablu.soumya.simplypdf.jsonengine.base

import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import org.json.JSONObject

abstract class ComposerConverter(protected val simplyPdfDocument: SimplyPdfDocument) {

    abstract fun generate(composeJsonObject: JSONObject)
    abstract fun getTypeHandler() : String

    inline fun <reified T> getProperties(compose: JSONObject, propertyNodeName: String) : T {
        if(!compose.has(propertyNodeName)) return T::class.java.newInstance()
        return Gson().fromJson(compose.getString(propertyNodeName), T::class.java)
    }
}