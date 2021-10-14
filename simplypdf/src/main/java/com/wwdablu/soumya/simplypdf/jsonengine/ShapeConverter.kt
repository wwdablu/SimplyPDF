package com.wwdablu.soumya.simplypdf.jsonengine

import android.graphics.Path
import android.text.TextUtils
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties
import org.json.JSONObject

internal class ShapeConverter(simplyPdfDocument: SimplyPdfDocument) : BaseConverter(simplyPdfDocument) {

    @Throws(Exception::class)
    public override fun generate(composer: Composer, compose: JSONObject) {
        if (composer !is ShapeComposer) {
            return
        }

        val shapePropertiesString = getProperties(compose)
        val shapeProperties = if (TextUtils.isEmpty(shapePropertiesString)) ShapeProperties()
            else Gson().fromJson(shapePropertiesString, ShapeProperties::class.java)

        when (compose.getString(Node.COMPOSER_SHAPE_SHAPE).lowercase()) {
            "circle" -> drawCircle(composer, compose, shapeProperties)
            "box" -> drawBox(composer, compose, shapeProperties)
            "freeform" -> drawFreeform(composer, compose, shapeProperties)
        }
    }

    @Throws(Exception::class)
    private fun drawCircle(
        shapeComposer: ShapeComposer,
        compose: JSONObject,
        shapeProperties: ShapeProperties
    ) {

        val radius = compose.getInt(Node.COMPOSER_SHAPE_RADIUS).toFloat()
        shapeComposer.drawCircle(radius, radius, radius, shapeProperties)
    }

    @Throws(Exception::class)
    private fun drawBox(
        shapeComposer: ShapeComposer,
        compose: JSONObject,
        shapeProperties: ShapeProperties
    ) {
        val width = compose.getInt(Node.COMPOSER_SHAPE_WIDTH)
        val height = compose.getInt(Node.COMPOSER_SHAPE_HEIGHT)
        shapeComposer.drawBox(0f, 0f, width.toFloat(), height.toFloat(), shapeProperties)
    }

    @Throws(Exception::class)
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
}