package com.wwdablu.soumya.simplypdf.composers.models;

import com.google.gson.annotations.SerializedName;
import com.wwdablu.soumya.simplypdf.composers.TableComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;

public class TableProperties extends UnitComposer.Properties {

    @SerializedName("width")
    public int borderWidth;

    @SerializedName("color")
    public String borderColor;

    @Override
    public String getPropId() {
        return TableComposer.class.getName() + "Properties";
    }
}
