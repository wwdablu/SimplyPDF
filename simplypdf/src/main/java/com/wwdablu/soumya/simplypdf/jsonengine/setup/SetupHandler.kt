package com.wwdablu.soumya.simplypdf.jsonengine.setup

import android.graphics.Color
import android.print.PrintAttributes
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin

internal class SetupHandler(private val simplyPdf: SimplyPdf,
                            private val setupJson: String) {

    fun apply() : SimplyPdfDocument {

        val setupInfo: Setup = Gson().fromJson(setupJson, Setup::class.java)

        //Apply margins
        simplyPdf.margin(if(setupInfo.margin == null) {
            Margin.none
        } else {
            Margin(setupInfo.margin.start, setupInfo.margin.top, setupInfo.margin.end, setupInfo.margin.bottom)
        })

        simplyPdf.paperSize(PrintAttributes.MediaSize.ISO_A4)
        simplyPdf.colorMode(DocumentInfo.ColorMode.COLOR)

        return simplyPdf.build().apply {
            setPageBackgroundColor(Color.parseColor(setupInfo.backgroundColor ?: "#FFFFFF"))
        }
    }
}