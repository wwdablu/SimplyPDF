package com.wwdablu.soumya.simplypdf.composers.models.cell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;

public class TextCell extends Cell {

    public final String text;
    public final TextProperties properties;

    public TextCell(@NonNull String text, @Nullable TextProperties properties, int width) {
        this.text = text;
        this.properties = properties;
        this.width = width;
    }
}
