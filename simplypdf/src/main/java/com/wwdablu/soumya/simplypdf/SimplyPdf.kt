package com.wwdablu.soumya.simplypdf

import android.content.Context
import android.print.PrintAttributes
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.DocumentInfo.ColorMode
import com.wwdablu.soumya.simplypdf.document.Margin
import com.wwdablu.soumya.simplypdf.document.PageModifier
import com.wwdablu.soumya.simplypdf.jsonengine.SimplyJson
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import java.io.File

class SimplyPdf private constructor(private val context: Context, outputPdf: File) {

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

    fun pageModifier(pageModifier: PageModifier) : SimplyPdf {
        document.pageModifiers.add(pageModifier)
        return this
    }

    fun build(): SimplyPdfDocument {
        document.build(context)
        return document
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun with(context: Context, outputPdf: File): SimplyPdf {
            return SimplyPdf(context, outputPdf)
        }

        @kotlin.jvm.JvmStatic
        suspend fun usingJson(
            context: Context,
            simplyPdfDocument: SimplyPdfDocument,
            payload: String,
            pageConverters: List<PageConverter>? = null,
            composerConverters: List<ComposerConverter>? = null
        ) {
            SimplyJson(context, payload).generateWith(simplyPdfDocument,
                pageConverters, composerConverters)
        }

        suspend fun usingJson(
            context: Context,
            outputPdf: File,
            payload: String,
            pageConverters: List<PageConverter>? = null,
            composerConverters: List<ComposerConverter>? = null
        ) {
            SimplyJson(context, payload).generateWith(SimplyPdf(context, outputPdf),
                pageConverters, composerConverters)
        }
    }
}