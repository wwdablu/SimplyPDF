package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument.Page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * Allows the developer to modify the PDF document. An instance of this can be obtained by using
 * the {@link SimplyPdf} class.
 */
public class SimplyPdfDocument {

    private File document;
    private Context context;

    private DocumentInfo documentInfo;
    private PrintedPdfDocument pdfDocument;
    private PrintAttributes printAttributes;
    private Page currentPage;

    private int currentPageNumber = 0;
    private int pageContentHeight = 0;

    private boolean finished;

    SimplyPdfDocument(@NonNull Context context, @NonNull File document) {
        this.document = document;
        this.context = context;
        documentInfo = new DocumentInfo();
    }

    void build() {
        build(context);
    }

    /**
     * Returns document information relating to the current pdf document.
     * @return Document information
     */
    @NonNull
    public DocumentInfo getDocumentInfo() {
        ensureNotFinished();
        return documentInfo;
    }

    public void insertEmptySpace(int height) {
        addContentHeight(height);
    }

    public int pageHeight() {
        ensureNotFinished();
        return getUsablePageHeight();
    }

    public int pageWidth() {
        ensureNotFinished();
        return getUsablePageWidth();
    }

    /**
     * Returns the current page number on which content is being drawn.
     * @return Current page number
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * Set the background color of the current page. It will apply it to the entire page.
     * @param color Color to be applied
     */
    public void setPageBackgroundColor(int color) {

        ensureNotFinished();

        Canvas canvas = getCurrentPage().getCanvas();
        canvas.save();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawRect(new RectF(0, 0, pdfDocument.getPageWidth(),
            pdfDocument.getPageHeight()), paint);
        canvas.restore();
    }

    /**
     * Complete all the tasks and write the PDF to the location provided.
     * @throws IOException IO Exception
     */
    public void finish() throws IOException {

        ensureNotFinished();

        pdfDocument.finishPage(getCurrentPage());
        pdfDocument.writeTo(new FileOutputStream(document));
        pdfDocument.close();
        finished = true;
    }

    public void build(@NonNull Context context) {
        printAttributes = new PrintAttributes.Builder()
                .setColorMode(documentInfo.resolveColorMode())
                .setMediaSize(documentInfo.getPaperSize())
                .setMinMargins(documentInfo.resolveMargin())
                .build();

        pdfDocument = new PrintedPdfDocument(context, printAttributes);
        currentPage = pdfDocument.startPage(getCurrentPageNumber());
        pageContentHeight = getTopMargin();
    }

    public int getLeftMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
                printAttributes.getMinMargins().getLeftMils();
    }

    public int getTopMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
                printAttributes.getMinMargins().getTopMils();
    }

    public int getRightMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
                printAttributes.getMinMargins().getRightMils();
    }

    public int getBottomMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
                printAttributes.getMinMargins().getBottomMils();
    }

    /**
     * Returns the current page being used
     * @return Current page object
     */
    public Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Creates a new page in the PDF document and resets the internal markers on the document.
     * @return The new page on which content will be drawn
     */
    public void newPage() {
        ensureNotFinished();
        pdfDocument.finishPage(currentPage);
        ++this.currentPageNumber;
        currentPage = pdfDocument.startPage(currentPageNumber);
        pageContentHeight = getTopMargin();
        addContentHeight(printAttributes.getMinMargins() == null ? 0 :
                printAttributes.getMinMargins().getTopMils());
    }

    /**
     * Returns the height of the content written in the current page.
     * @return Height of content for the current page
     */
    public int getPageContentHeight() {
        return pageContentHeight;
    }

    /**
     * Adds the height of the content drawn currently to the total height of content already
     * present in the current page.
     * @param pageContentHeight Page Content Height
     */
    public void addContentHeight(int pageContentHeight) {
        this.pageContentHeight += pageContentHeight;
    }

    /**
     * Returns the height of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable height of the page
     */
    public int getUsablePageHeight() {
        return pdfDocument == null ? 0 : (pdfDocument.getPageContentRect().height() -
                (getTopMargin() + getBottomMargin()));
    }

    /**
     * Returns the width of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable width of the page
     */
    public int getUsablePageWidth() {
        return pdfDocument.getPageWidth() - (getLeftMargin() + getRightMargin());
    }

    void ensureNotFinished() {
        if(finished) throw new IllegalStateException("Cannot use as finish has been called.");
    }
}
