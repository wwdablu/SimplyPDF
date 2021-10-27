package com.wwdablu.soumya.simplypdf.jsonengine

import android.content.Context
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import com.wwdablu.soumya.simplypdf.jsonengine.composerconverters.*
import com.wwdablu.soumya.simplypdf.jsonengine.pagemodifiers.HeaderConverter
import com.wwdablu.soumya.simplypdf.jsonengine.setup.SetupHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

internal class SimplyJson(private val context: Context, private val payload: String) {
    
    private lateinit var simplyPdfDocument: SimplyPdfDocument

    private val pageConverters: HashMap<String, PageConverter> by lazy { HashMap<String, PageConverter>().also { map ->
            HeaderConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
        }
    }

    private val composerConverters: HashMap<String, ComposerConverter> by lazy { HashMap<String, ComposerConverter>().also { map ->
            TextConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
            ImageConverter(context, simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
            TableConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
            NewPageConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
            SpaceConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
        }
    }

    suspend fun generateWith(pdfBuilder: SimplyPdf,
                             pageConverters: List<PageConverter>? = null,
                             composerConverters: List<ComposerConverter>? = null) {

        val contentArray: JSONArray? = JSONObject(payload).getJSONArray(Node.PAGE)
        if (contentArray == null || contentArray.length() == 0) {
            return
        }

        for(index in 0 until contentArray.length()) {
            if((contentArray.getJSONObject(index).has(Node.TYPE)) &&
                (contentArray.getJSONObject(index).getString(Node.TYPE).lowercase() == Node.TYPE_PAGE_SETUP)) {

                generateWith(SetupHandler(pdfBuilder, contentArray.getJSONObject(index).toString())
                    .apply(), pageConverters, composerConverters)
                return
            }
        }

        generateWith(pdfBuilder.build(), pageConverters, composerConverters)
    }

    @JvmOverloads
    suspend fun generateWith(simplyPdfDocument: SimplyPdfDocument,
                             pageConverters: List<PageConverter>? = null,
                             composerConverters: List<ComposerConverter>? = null) {

        this.simplyPdfDocument = simplyPdfDocument

        pageConverters?.forEach {
            this.pageConverters[it.getTypeHandler()] = it
        }

        composerConverters?.forEach {
            this.composerConverters[it.getTypeHandler()] = it
        }

        withContext(Dispatchers.IO) {
            parsePagePayload()
            parseContentPayload()
            simplyPdfDocument.finish()
        }
    }

    private fun parsePagePayload() {

        val contentArray: JSONArray? = JSONObject(payload).getJSONArray(Node.PAGE)
        if (contentArray == null || contentArray.length() == 0) {
            return
        }

        for (index in 0 until contentArray.length()) {
            val compose: JSONObject = contentArray.getJSONObject(index) ?: continue
            pageConverters[compose.getString(Node.TYPE).lowercase()]?.generate(compose)
        }
    }

    private fun parseContentPayload() {

        val contentArray: JSONArray? = JSONObject(payload).getJSONArray(Node.CONTENTS)
        if (contentArray == null || contentArray.length() == 0) {
            return
        }

        for (index in 0 until contentArray.length()) {
            val compose: JSONObject = contentArray.getJSONObject(index) ?: continue
            composerConverters[compose.getString(Node.TYPE).lowercase()]?.generate(compose)
        }
    }
}