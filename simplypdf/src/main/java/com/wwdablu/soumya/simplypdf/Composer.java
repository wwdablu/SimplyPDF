package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

abstract class Composer {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    int DEFAULT_SPACING = 10;

    SimplyPdfDocument simplyPdfDocument;

    protected abstract String getComposerName();

    public void setSpacing(int spacing) {
        this.DEFAULT_SPACING = spacing;
    }

    public void setSimplyPdfDocument(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
    }

    protected void insertEmptyLine() {
        simplyPdfDocument.addContentHeight(DEFAULT_SPACING);
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
