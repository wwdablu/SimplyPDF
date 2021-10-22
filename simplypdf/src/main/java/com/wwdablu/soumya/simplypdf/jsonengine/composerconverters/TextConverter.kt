package com.wwdablu.soumya.simplypdf.jsonengine.composerconverters

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.jsonengine.Node
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class TextConverter(simplyPdfDocument: SimplyPdfDocument) : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {

        //Check if properties has been defined
        val textProperties:TextProperties = getProperties(composeJsonObject, Node.TYPE_PROPERTIES)
        simplyPdfDocument.text.write(composeJsonObject.getString(Node.COMPOSER_TEXT_CONTENT), textProperties)
    }

    override fun getTypeHandler(): String = "shape"
}