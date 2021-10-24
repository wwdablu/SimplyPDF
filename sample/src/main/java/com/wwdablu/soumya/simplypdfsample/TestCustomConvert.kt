package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.properties.ShapeProperties
import com.wwdablu.soumya.simplypdf.document.PageModifier
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import org.json.JSONObject

class TestCustomConvert(context: Context) : CommonActions(context) {

    inner class HeaderLinePageModifier : PageModifier() {
        override fun render(simplyPdfDocument: SimplyPdfDocument) {
            simplyPdfDocument.shape.drawBox(simplyPdfDocument.usablePageWidth.toFloat() - simplyPdfDocument.endMargin, 2F, ShapeProperties().apply {
                lineColor = "#000000"
                alignment = Composer.Alignment.CENTER
            })
            simplyPdfDocument.insertEmptySpace(25)
        }
    }

    inner class WatermarkPageModifier : PageModifier() {
        override fun render(simplyPdfDocument: SimplyPdfDocument) {
            TODO("Not yet implemented")
        }
    }

    inner class WatermarkJsonConverter : PageConverter() {

        override fun generate(compose: JSONObject) {
            WatermarkPageModifier().render(simplyPdfDocument)
            val t: Int = getProperties(compose, "properties")
        }

        override fun getTypeHandler(): String = "watermark"
    }
}