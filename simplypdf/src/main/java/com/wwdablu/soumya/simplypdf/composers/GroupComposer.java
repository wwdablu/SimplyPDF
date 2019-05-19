package com.wwdablu.soumya.simplypdf.composers;

public abstract class GroupComposer extends Composer {

    public int resolveCellWidth(int widthPercent) {
        return simplyPdfDocument.pageWidth() / (100 / widthPercent);
    }
}
