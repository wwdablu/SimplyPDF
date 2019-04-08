package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.graphics.pdf.PdfDocument.Page;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;

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

    private TextComposer textComposer;
    private ShapeComposer shapeComposer;
    private ImageComposer imageComposer;

    private int currentPageNumber = 0;
    private int pageContentHeight = 0;

    SimplyPdfDocument(@NonNull Context context, @NonNull File document) {
        documentInfo = new DocumentInfo();
        this.document = document;
        this.context = context;
    }

    /**
     * Returns document information relating to the current pdf document.
     * @return Document information
     */
    @NonNull
    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    /**
     * Returns a helper to write texts
     * @return Text writer
     */
    @NonNull
    public TextComposer getTextComposer() {

        if(textComposer == null) {
            textComposer = new TextComposer(this);
        }

        return textComposer;
    }

    /**
     * Returns a shape drawing helper.
     * @return Shape drawer
     */
    @NonNull
    public ShapeComposer getShapeComposer() {

        if(shapeComposer == null) {
            shapeComposer = new ShapeComposer(this);
        }

        return shapeComposer;
    }

    /**
     * Returns an image drawing helper.
     * @return Image renderer
     */
    @NonNull
    public ImageComposer getImageComposer() {

        if(imageComposer == null) {
            imageComposer = new ImageComposer(this);
        }

        return imageComposer;
    }

    public void insertEmptyLine() {
        getTextComposer().insertEmptyLine();
    }

    public int pageHeight() {
        return getUsablePageHeight();
    }

    public int pageWidth() {
        return getUsablePageWidth();
    }

    public void finish() throws IOException {
        pdfDocument.finishPage(currentPage);
        pdfDocument.writeTo(new FileOutputStream(document));
        pdfDocument.close();

        if(textComposer != null) {
            textComposer.clean();
        }
    }

    /*
     *
     * --- STOP ---
     *     ANYTHING BELOW IS MAINTAINED INTERNALLY BY SIMPLY PDF AND IS NOT EXPOSED
     * --- STOP ---
     *
     */

    void build() {
        printAttributes = new PrintAttributes.Builder()
                .setColorMode(documentInfo.resolveColorMode())
                .setMediaSize(documentInfo.getPaperSize())
                .setMinMargins(documentInfo.resolveMargin())
                .build();

        pdfDocument = new PrintedPdfDocument(context, printAttributes);
        currentPage = pdfDocument.startPage(getCurrentPageNumber());
        pageContentHeight = getTopMargin();
    }

    int getLeftMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getLeftMils();
    }

    int getTopMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getTopMils();
    }

    int getRightMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getRightMils();
    }

    int getBottomMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getBottomMils();
    }

    /**
     * Returns the current page number on which content is being drawn.
     * @return Current page number
     */
    int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * Returns the current page being used
     * @return
     */
    Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Creates a new page in the PDF document and resets the internal markers on the document.
     * @return The new page on which content will be drawn
     */
    Page newPage() {
        pdfDocument.finishPage(currentPage);
        ++this.currentPageNumber;
        currentPage = pdfDocument.startPage(currentPageNumber);
        pageContentHeight = getTopMargin();
        addContentHeight(printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getTopMils());
        return currentPage;
    }

    /**
     * Returns the height of the content written in the current page.
     * @return Height of content for the current page
     */
    int getPageContentHeight() {
        return pageContentHeight;
    }

    /**
     * Adds the height of the content drawn currently to the total height of content already
     * present in the current page.
     * @param pageContentHeight Page Content Height
     */
    void addContentHeight(int pageContentHeight) {
        this.pageContentHeight += pageContentHeight;
    }

    /**
     * Returns the height of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable height of the page
     */
    int getUsablePageHeight() {
        return pdfDocument == null ? 0 : (pdfDocument.getPageContentRect().height() -
            (getTopMargin() + getBottomMargin()));
    }

    /**
     * Returns the width of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable width of the page
     */
    int getUsablePageWidth() {
        return pdfDocument.getPageWidth() - (getLeftMargin() + getRightMargin());
    }
}
