package com.wwdablu.soumya.simplypdf.jsonengine.composerconverters

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class NewPageConverter(simplyPdfDocument: SimplyPdfDocument) :  ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {
        simplyPdfDocument.newPage()
    }

    override fun getTypeHandler(): String  = "newpage"
}