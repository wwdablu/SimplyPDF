package com.wwdablu.soumya.simplypdf

import android.content.Context
import android.print.PrintAttributes
import com.wwdablu.soumya.simplypdf.documentinfo.DocumentInfo
import com.wwdablu.soumya.simplypdf.documentinfo.DocumentInfo.ColorMode
import com.wwdablu.soumya.simplypdf.documentinfo.Margin
import com.wwdablu.soumya.simplypdf.jsonengine.SimplyJson
import java.io.File

class SimplyPdf private constructor(context: Context, outputPdf: File) {

    private val document: SimplyPdfDocument = SimplyPdfDocument(context, outputPdf)

    fun colorMode(colorMode: ColorMode): SimplyPdf {
        document.documentInfo.colorMode = colorMode
        return this
    }

    fun margin(margins: Margin): SimplyPdf {
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

        @Deprecated("Will be removed in future", ReplaceWith(
            "usingJson(context, simplyPdfDocument, payload)",
            "com.wwdablu.soumya.simplypdf.SimplyPdf.Companion.usingJson"))
        suspend fun use(
            context: Context,
            simplyPdfDocument: SimplyPdfDocument,
            payload: String
        ) {
            usingJson(context, simplyPdfDocument, payload)
        }

        @kotlin.jvm.JvmStatic
        suspend fun usingJson(
            context: Context,
            simplyPdfDocument: SimplyPdfDocument,
            payload: String
        ) {
            SimplyJson(context, payload).generateWith(simplyPdfDocument)
        }
    }
}