package com.wwdablu.soumya.simplypdf.composers.models;

import com.google.gson.annotations.SerializedName;
import com.wwdablu.soumya.simplypdf.composers.Composer;
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;

public class ShapeProperties extends UnitComposer.Properties {

    @SerializedName("linecolor")
    public String lineColor;

    @SerializedName("linewidth")
    public int lineWidth;

    @SerializedName("shouldfill")
    public boolean shouldFill;

    public Composer.Alignment alignment;

    public ShapeProperties() {
        lineColor = "#000000";
        lineWidth = 1;
        shouldFill = false;
        alignment = Composer.Alignment.LEFT;
    }

    @Override
    public String getPropId() {
        return ShapeComposer.class.getName() + "Properties";
    }
}
