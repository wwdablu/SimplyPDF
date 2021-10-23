package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.os.Environment
import android.print.PrintAttributes
import android.text.Layout
import android.widget.Toast
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.properties.ShapeProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin
import com.wwdablu.soumya.simplypdf.document.PageHeader
import com.wwdablu.soumya.simplypdf.document.PageModifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

abstract class CommonActions(protected val context: Context) {

    protected lateinit var simplyPdfDocument: SimplyPdfDocument

    protected fun createSimplyPdfDocument() {

        simplyPdfDocument = SimplyPdf.with(context,
                File(Environment.getExternalStorageDirectory().absolutePath + "/test.pdf"))
            .colorMode(DocumentInfo.ColorMode.COLOR)
            .paperSize(PrintAttributes.MediaSize.ISO_A4)
            .margin(Margin(15U, 15U, 15U, 15U))
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
            .pageModifier(PageHeader(LinkedList<Cell>().apply {
                add(TextCell("PDF Generated Using SimplyPDF", TextProperties().apply {
                    textSize = 24
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
                add(TextCell("Version 2.0.0", TextProperties().apply {
                    textSize = 18
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
            }))
            .pageModifier(HeaderLinePageModifier())
            .build()
    }

    protected fun finishDoc() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = simplyPdfDocument.finish()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PDF $result", Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected inner class HeaderLinePageModifier : PageModifier() {
        override fun render(simplyPdfDocument: SimplyPdfDocument) {
            simplyPdfDocument.shape.drawBox(simplyPdfDocument.usablePageWidth.toFloat(), 2F, ShapeProperties().apply {
                lineColor = "#000000"
                alignment = Composer.Alignment.CENTER
            })
            simplyPdfDocument.insertEmptySpace(25)
        }
    }
}