package com.wwdablu.soumya.simplypdf

import android.content.Context
import android.print.PrintAttributes
import com.wwdablu.soumya.simplypdf.DocumentInfo.ColorMode
import com.wwdablu.soumya.simplypdf.jsonengine.SimplyJson
import java.io.File

class SimplyPdf private constructor(context: Context, outputPdf: File) {

    private val document: SimplyPdfDocument = SimplyPdfDocument(context, outputPdf)

    fun colorMode(colorMode: ColorMode?): SimplyPdf {
        document.documentInfo.colorMode = colorMode
        return this
    }

    fun margin(margins: DocumentInfo.Margins?): SimplyPdf {
        document.documentInfo.margins = margins
        return this
    }

    fun paperSize(paperSize: PrintAttributes.MediaSize): SimplyPdf {
        document.documentInfo.paperSize = paperSize
        return this
    }

    fun paperOrientation(orientation: DocumentInfo.Orientation): SimplyPdf {
        document.documentInfo.orientation = orientation
        return this
    }

    fun build(): SimplyPdfDocument {
        document.build()
        return document
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun with(context: Context, outputPdf: File): SimplyPdf {
            return SimplyPdf(context, outputPdf)
        }

        @kotlin.jvm.JvmStatic
        suspend fun use(
            context: Context,
            simplyPdfDocument: SimplyPdfDocument,
            payload: String
        ) {
            SimplyJson(context, payload).generateWith(simplyPdfDocument)
        }
    }
}