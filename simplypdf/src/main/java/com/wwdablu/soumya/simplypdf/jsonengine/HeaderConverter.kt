package com.wwdablu.soumya.simplypdf.jsonengine

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.document.PageHeader
import com.wwdablu.soumya.simplypdf.jsonengine.base.PageConverter
import org.json.JSONObject
import java.util.*

internal class HeaderConverter(private val simplyPdfDocument: SimplyPdfDocument) : PageConverter() {

    override fun getTypeHandler(): String = "header"

    override fun generate(compose: JSONObject) {

        val tableConverter = TableConverter(simplyPdfDocument)
        val cellList: LinkedList<Cell> = LinkedList()

        val contentArray = compose.getJSONArray(Node.CONTENTS)
        for(index in 0 until contentArray.length()) {
            cellList.add(tableConverter.cellConverter(contentArray.getJSONObject(index),
                simplyPdfDocument.table))
        }

        PageHeader(cellList).apply {
            //This will render for this first page only, rest will be handled automatically
            render(simplyPdfDocument)
            simplyPdfDocument.pageModifiers.add(this)
        }
    }
}