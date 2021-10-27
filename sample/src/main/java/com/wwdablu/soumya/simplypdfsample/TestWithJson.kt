package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.os.Environment
import android.print.PrintAttributes
import android.util.Log
import android.widget.Toast
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin
import kotlinx.coroutines.*
import java.io.File

class TestWithJson {

    fun testWithJson(context: Context) {
        val simplyPdfDocument = SimplyPdf.with(context,
                File(Environment.getExternalStorageDirectory().absolutePath + "/json_to_pdf.pdf"))
            .colorMode(DocumentInfo.ColorMode.COLOR)
            .paperSize(PrintAttributes.MediaSize.ISO_A4)
            .margin(Margin(15U, 15U, 15U, 15U))
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
            .build()

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(MainActivity::class.java.simpleName, "JSON to PDF exception.", throwable)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "JSON to PDF Failed because, ${throwable.message}", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            SimplyPdf.usingJson(context, simplyPdfDocument, JSONStruct.payload)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "JSON to PDF Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun testWithJsonAndSetup(context: Context) {

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(MainActivity::class.java.simpleName, "JSON to PDF exception.", throwable)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "JSON to PDF Failed because, ${throwable.message}", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            //usingJson(this@MainActivity, simplyPdfDocument, JSONStruct.payload)
            SimplyPdf.usingJson(context,
                File(Environment.getExternalStorageDirectory().absolutePath + "/json_to_pdf.pdf"),
                JSONStruct.payload
            )

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "JSON to PDF Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}