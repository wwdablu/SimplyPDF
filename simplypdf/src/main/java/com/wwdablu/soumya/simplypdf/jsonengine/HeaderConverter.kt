package com.wwdablu.soumya.simplypdf.jsonengine

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import org.json.JSONObject

internal class HeaderConverter(simplyPdfDocument: SimplyPdfDocument) : PageConverter() {

    override fun getTypeHandler(): String = "header"

    override fun generate(compose: JSONObject) {
        //
    }
}