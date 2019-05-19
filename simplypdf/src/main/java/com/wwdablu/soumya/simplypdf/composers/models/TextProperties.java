package com.wwdablu.soumya.simplypdf.composers.models;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;

public class TextProperties extends UnitComposer.Properties {

    @SerializedName("color")
    public String textColor;

    @SerializedName("size")
    public int textSize;

    @SerializedName("isBullet")
    public boolean isBullet;

    @SerializedName("bulletSymbol")
    public String bulletSymbol;

    @SerializedName("underline")
    public boolean underline;

    @SerializedName("strikethrough")
    public boolean strikethrough;

    public Typeface typeface;
    public Layout.Alignment alignment;

    public TextProperties() {
        textColor = "#FFFFFF";
        textSize = 10;
        typeface = null;
        alignment = Layout.Alignment.ALIGN_NORMAL;
        isBullet = false;
        bulletSymbol = "";
        underline = false;
        strikethrough = false;
    }

    public Layout.Alignment getAlignment() {

        if(alignment == null) {
            alignment = Layout.Alignment.ALIGN_NORMAL;
        }

        return this.alignment;
    }

    public String getBulletSymbol() {

        if(bulletSymbol == null || TextUtils.isEmpty(bulletSymbol)) {
            this.bulletSymbol = "";
            return "";
        }

        if(bulletSymbol.length() >= 2) {
            bulletSymbol = bulletSymbol.substring(0, 1);
        }

        return this.bulletSymbol;
    }

    @Override
    public String getPropId() {
        return TextComposer.class.getName() + "Properties";
    }
}
