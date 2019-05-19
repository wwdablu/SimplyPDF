package com.wwdablu.soumya.simplypdf.composers.models;

import com.google.gson.annotations.SerializedName;
import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;

public class ImageProperties extends UnitComposer.Properties {

    @SerializedName("imageurl")
    public String imageUrl;

    public UnitComposer.Alignment alignment;

    public ImageProperties() {
        alignment = UnitComposer.Alignment.LEFT;
    }

    @Override
    public String getPropId() {
        return ImageComposer.class.getName() + "Properties";
    }
}
