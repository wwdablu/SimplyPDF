package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.document.PageModifier
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import org.json.JSONObject

class TestCustomConvert(context: Context) : CommonActions(context) {

    inner class HeaderLinePageModifier : PageModifier() {
        override fun render(simplyPdfDocument: SimplyPdfDocument) {
            simplyPdfDocument.apply {
                val rect = RectF(usablePageWidth/4f,
                    pageContentHeight.toFloat(),
                    usablePageWidth - (usablePageWidth/4f),
                    (pageContentHeight + 1).toFloat())

                currentPage.canvas.drawRect(rect, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.BLACK
                }
                )

                addContentHeight(rect.height().toInt())
            }
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