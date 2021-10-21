package com.wwdablu.soumya.simplypdf.jsonengine

import android.content.Context
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            ShapeConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
            TableConverter(simplyPdfDocument).apply {
                map[getTypeHandler()] = this
            }
        }
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

        val contentArray: JSONArray? = JSONObject(payload).getJSONArray(Node.CONTENT)
        if (contentArray == null || contentArray.length() == 0) {
            return
        }

        for (index in 0 until contentArray.length()) {
            val compose: JSONObject = contentArray.getJSONObject(index) ?: continue
            composerConverters[compose.getString(Node.TYPE).lowercase()]?.generate(compose)
        }
    }
}