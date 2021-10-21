package com.wwdablu.soumya.simplypdf.jsonengine

import android.graphics.Path
import android.text.TextUtils
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer
import com.wwdablu.soumya.simplypdf.composers.properties.ShapeProperties
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject

internal class ShapeConverter(simplyPdfDocument: SimplyPdfDocument) : ComposerConverter(simplyPdfDocument) {

    override fun generate(composeJsonObject: JSONObject) {

        val shapePropertiesString = getProperties(composeJsonObject)
        val shapeProperties = if (TextUtils.isEmpty(shapePropertiesString)) ShapeProperties()
            else Gson().fromJson(shapePropertiesString, ShapeProperties::class.java)

        val composer = simplyPdfDocument.shape

        when (composeJsonObject.getString(Node.COMPOSER_SHAPE_SHAPE).lowercase()) {
            "circle" -> drawCircle(composer, composeJsonObject, shapeProperties)
            "box" -> drawBox(composer, composeJsonObject, shapeProperties)
            "freeform" -> drawFreeform(composer, composeJsonObject, shapeProperties)
        }
    }

    private fun drawCircle(
        shapeComposer: ShapeComposer,
        compose: JSONObject,
        shapeProperties: ShapeProperties
    ) {

        val radius = compose.getInt(Node.COMPOSER_SHAPE_RADIUS).toFloat()
        shapeComposer.drawCircle(radius, shapeProperties)
    }

    private fun drawBox(
        shapeComposer: ShapeComposer,
        compose: JSONObject,
        shapeProperties: ShapeProperties
    ) {
        val width = compose.getInt(Node.COMPOSER_SHAPE_WIDTH)
        val height = compose.getInt(Node.COMPOSER_SHAPE_HEIGHT)
        shapeComposer.drawBox(width.toFloat(), height.toFloat(), shapeProperties)
    }

    private fun drawFreeform(
        shapeComposer: ShapeComposer,
        compose: JSONObject,
        shapeProperties: ShapeProperties
    ) {
        val pointsArray = compose.getJSONArray(Node.COMPOSER_SHAPE_POINTS)
        val pointsArrayLength = pointsArray.length()
        val path = Path()
        for (pointIndex in 0 until pointsArrayLength) {
            val lineObject = pointsArray.getJSONObject(pointIndex)
            val lineArray = lineObject.getJSONArray(Node.COMPOSER_SHAPE_LINE)
            if (pointIndex == 0) {
                path.moveTo(lineArray.getInt(0).toFloat(), lineArray.getInt(1).toFloat())
            } else {
                path.lineTo(lineArray.getInt(0).toFloat(), lineArray.getInt(1).toFloat())
            }
        }
        shapeComposer.freeform(path, shapeProperties)
    }

    override fun getTypeHandler(): String = "shape"
}