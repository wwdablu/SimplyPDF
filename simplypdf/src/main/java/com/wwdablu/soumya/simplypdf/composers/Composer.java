package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;

public abstract class Composer {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    protected static final int DEFAULT_SPACING = 10;

    private int defaultSpacing = 10;

    protected SimplyPdfDocument simplyPdfDocument;

    public abstract String getComposerName();

    public void setSpacing(int spacing) {

        if(spacing <= -1) spacing = DEFAULT_SPACING;
        this.defaultSpacing = spacing;
    }

    protected int getSpacing() {
        return this.defaultSpacing;
    }

    public void setSimplyPdfDocument(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
    }

    protected void insertEmptyLine() {
        simplyPdfDocument.addContentHeight(defaultSpacing);
    }

    protected final Canvas getPageCanvas() {
        return simplyPdfDocument.getCurrentPage().getCanvas();
    }

    protected final boolean canFitContentInPage(int contentHeight) {

        return simplyPdfDocument.getUsablePageHeight() >=
            (contentHeight + simplyPdfDocument.getPageContentHeight());
    }

    protected final int getTopSpacing(int intendedSpace) {

        return (simplyPdfDocument.getPageContentHeight() ==
            simplyPdfDocument.getTopMargin() ? 0 : intendedSpace);
    }

    protected final int alignmentCanvasTranslation(Alignment alignment, int width) {

        int xTranslate = 0;
        switch (alignment) {
            case CENTER:
                xTranslate = (simplyPdfDocument.getUsablePageWidth() - width) /2;
                break;

            case RIGHT:
                xTranslate = simplyPdfDocument.getUsablePageWidth() - width;
                break;
        }

        return xTranslate;
    }

    void clean() {
        simplyPdfDocument = null;
    }
}
