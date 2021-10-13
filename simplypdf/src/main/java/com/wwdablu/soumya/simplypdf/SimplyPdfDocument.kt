package com.wwdablu.soumya.simplypdf

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Allows the developer to modify the PDF document. An instance of this can be obtained by using
 * the [SimplyPdf] class.
 */
class SimplyPdfDocument internal constructor(
    private val context: Context,
    private val document: File
) {
    val documentInfo: DocumentInfo = DocumentInfo()
    private lateinit var pdfDocument: PrintedPdfDocument
    private lateinit var printAttributes: PrintAttributes

    /**
     * Returns the current page being used
     * @return Current page object
     */
    lateinit var currentPage: PdfDocument.Page
        private set

    /**
     * Returns the current page number on which content is being drawn.
     * @return Current page number
     */
    var currentPageNumber = 0
        private set

    /**
     * Returns the height of the content written in the current page.
     * @return Height of content for the current page
     */
    var pageContentHeight = 0
        private set
    private var finished = false
    fun build() {
        build(context)
    }

    fun insertEmptySpace(height: Int) {
        addContentHeight(height)
    }

    fun pageHeight(): Int {
        ensureNotFinished()
        return usablePageHeight
    }

    fun pageWidth(): Int {
        ensureNotFinished()
        return usablePageWidth
    }

    /**
     * Set the background color of the current page. It will apply it to the entire page.
     * @param color Color to be applied
     */
    fun setPageBackgroundColor(color: Int) {
        ensureNotFinished()
        val canvas = currentPage.canvas
        canvas.save()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        canvas.drawRect(
            RectF(
                0F, 0F, pdfDocument.pageWidth.toFloat(),
                pdfDocument.pageHeight.toFloat()
            ), paint
        )
        canvas.restore()
    }

    /**
     * Complete all the tasks and write the PDF to the location provided.
     * @throws IOException IO Exception
     */
    @Throws(IOException::class)
    fun finish() {
        ensureNotFinished()
        pdfDocument.finishPage(currentPage)
        pdfDocument.writeTo(FileOutputStream(document))
        pdfDocument.close()
        finished = true
    }

    fun build(context: Context) {
        printAttributes = PrintAttributes.Builder()
            .setColorMode(documentInfo.resolveColorMode())
            .setMediaSize(documentInfo.paperSize)
            .setMinMargins(documentInfo.resolveMargin())
            .build()
        pdfDocument = PrintedPdfDocument(context, printAttributes)
        currentPage = pdfDocument.startPage(currentPageNumber)
        pageContentHeight = topMargin
    }

    val leftMargin: Int
        get() = if (printAttributes.minMargins == null) 0 else printAttributes.minMargins?.leftMils ?: 0
    val topMargin: Int
        get() = if (printAttributes.minMargins == null) 0 else printAttributes.minMargins?.topMils ?: 0
    val rightMargin: Int
        get() = if (printAttributes.minMargins == null) 0 else printAttributes.minMargins?.rightMils ?: 0
    val bottomMargin: Int
        get() = if (printAttributes.minMargins == null) 0 else printAttributes.minMargins?.bottomMils ?: 0

    /**
     * Creates a new page in the PDF document and resets the internal markers on the document.
     * @return The new page on which content will be drawn
     */
    fun newPage() {
        ensureNotFinished()
        pdfDocument.finishPage(currentPage)
        ++currentPageNumber
        currentPage = pdfDocument.startPage(currentPageNumber)
        pageContentHeight = topMargin
        addContentHeight(
            if (printAttributes.minMargins == null) 0 else printAttributes.minMargins?.topMils ?: 0
        )
    }

    /**
     * Adds the height of the content drawn currently to the total height of content already
     * present in the current page.
     * @param pageContentHeight Page Content Height
     */
    fun addContentHeight(pageContentHeight: Int) {
        this.pageContentHeight += pageContentHeight
    }

    /**
     * Returns the height of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable height of the page
     */
    val usablePageHeight: Int
        get() = pdfDocument.pageContentRect.height() - (topMargin + bottomMargin)

    /**
     * Returns the width of the page on which content can be displayed. It taken into account the
     * margins on the page.
     * @return Usable width of the page
     */
    val usablePageWidth: Int
        get() = pdfDocument.pageWidth - (leftMargin + rightMargin)

    fun ensureNotFinished() {
        check(!finished) { "Cannot use as finish has been called." }
    }
}