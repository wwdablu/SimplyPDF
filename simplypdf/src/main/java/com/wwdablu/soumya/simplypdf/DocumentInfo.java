package com.wwdablu.soumya.simplypdf;

import android.print.PrintAttributes;

public final class DocumentInfo {

    public enum ColorMode {
        MONO,
        COLOR
    }

    public enum Margins {
        NONE,
        NARROW,
        DEFAULT
    }

    private PrintAttributes.MediaSize paperSize = PrintAttributes.MediaSize.ISO_A4;
    private ColorMode colorMode = ColorMode.COLOR;
    private Margins   margins   = Margins.DEFAULT;

    DocumentInfo() {
        //
    }

    public PrintAttributes.MediaSize getPaperSize() {
        return paperSize;
    }

    void setPaperSize(PrintAttributes.MediaSize paperSize) {
        this.paperSize = paperSize;
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public Margins getMargins() {
        return margins;
    }

    void setMargins(Margins margins) {
        this.margins = margins;
    }

    /*
     *
     * --- STOP ---
     *     ANYTHING BELOW IS MAINTAINED INTERNALLY BY SIMPLY PDF AND IS NOT EXPOSED
     * --- STOP ---
     *
     */
    
    int resolveColorMode() {
        switch (colorMode) {
            case COLOR:
                return PrintAttributes.COLOR_MODE_COLOR;
                
            default:
            case MONO:
                return PrintAttributes.COLOR_MODE_MONOCHROME;
        }
    }

    PrintAttributes.Margins resolveMargin() {
        switch (margins) {

            case NONE:
                return new PrintAttributes.Margins(0, 0, 0, 0);

            case NARROW:
                return new PrintAttributes.Margins(10, 10, 10, 10);

            default:
            case DEFAULT:
                return new PrintAttributes.Margins(20, 15, 20, 15);
        }
    }
}
