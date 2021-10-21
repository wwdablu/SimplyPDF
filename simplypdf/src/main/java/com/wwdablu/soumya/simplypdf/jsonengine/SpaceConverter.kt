package com.wwdablu.soumya.simplypdf.jsonengine

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class SpaceConverter(simplyPdfDocument: SimplyPdfDocument) : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {
        simplyPdfDocument.insertEmptySpace(composeJsonObject.getInt(Node.SPACE_HEIGHT))
    }

    override fun getTypeHandler(): String = "space"
}