package com.wwdablu.soumya.simplypdf.jsonengine;

import android.graphics.Path;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.composers.Composer;
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer;
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties;

import org.json.JSONArray;
import org.json.JSONObject;

class ShapeConverter extends BaseConverter {

    @Override
    protected void generate(Composer composer, JSONObject compose) throws Exception {

        if(!(composer instanceof ShapeComposer)) {
            return;
        }

        ShapeComposer shapeComposer = (ShapeComposer) composer;

        String shapePropertiesString = getProperties(compose);
        ShapeProperties shapeProperties = TextUtils.isEmpty(shapePropertiesString) ? null :
                new Gson().fromJson(shapePropertiesString, ShapeProperties.class);

        String shapeToDraw = compose.getString(Node.COMPOSER_SHAPE_SHAPE).toLowerCase();
        switch (shapeToDraw) {

            case "circle":
                drawCircle(shapeComposer, compose, shapeProperties);
                break;

            case "box":
                drawBox(shapeComposer, compose, shapeProperties);
                break;

            case "freeform":
                drawFreeform(shapeComposer, compose, shapeProperties);
                break;
        }
    }

    private void drawCircle(ShapeComposer shapeComposer,
                            JSONObject compose,
                            ShapeProperties shapeProperties) throws Exception {

        int radius = compose.getInt(Node.COMPOSER_SHAPE_RADIUS);
        shapeComposer.drawCircle(radius, shapeProperties);
    }

    private void drawBox(ShapeComposer shapeComposer,
                         JSONObject compose,
                         ShapeProperties shapeProperties) throws Exception {

        int width = compose.getInt(Node.COMPOSER_SHAPE_WIDTH);
        int height = compose.getInt(Node.COMPOSER_SHAPE_HEIGHT);

        shapeComposer.drawBox(width, height, shapeProperties);
    }

    private void drawFreeform(ShapeComposer shapeComposer, JSONObject compose, ShapeProperties shapeProperties) throws Exception {

        JSONArray pointsArray = compose.getJSONArray(Node.COMPOSER_SHAPE_POINTS);
        int pointsArrayLength = pointsArray.length();

        Path path = new Path();
        for(int pointIndex = 0; pointIndex < pointsArrayLength; pointIndex++) {

            JSONObject lineObject = pointsArray.getJSONObject(pointIndex);
            JSONArray lineArray = lineObject.getJSONArray(Node.COMPOSER_SHAPE_LINE);

            if(pointIndex == 0) {
                path.moveTo(lineArray.getInt(0), lineArray.getInt(1));
            } else {
                path.lineTo(lineArray.getInt(0), lineArray.getInt(1));
            }
        }

        shapeComposer.freeform(path, shapeProperties);
    }
}
