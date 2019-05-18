package com.wwdablu.soumya.simplypdf.composers.models;

import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;

public class ImageProperties extends UnitComposer.Properties {

    public UnitComposer.Alignment alignment;

    public ImageProperties() {
        alignment = UnitComposer.Alignment.LEFT;
    }

    @Override
    public String getPropId() {
        return ImageComposer.class.getName() + "Properties";
    }
}
