package com.wwdablu.soumya.simplypdf.composers

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell

abstract class UnitComposer(simplyPdfDocument: SimplyPdfDocument) : Composer(simplyPdfDocument) {

    /**
     * Method to provide the amount of pixels to be shifted in X axis within the cell to
     * honour the alignment provided for the UnitComposer
     *
     * @param alignment Alignment to be used for the UnitComposer within the cell
     * @param cell Cell to be used for the purpose of calculation
     * @param xMargin Margins to be applied in X axis on both the sides of the cell
     */
    protected fun cellAlignmentTranslateX(alignment: Alignment, cell: Cell, xMargin: Int) : Int {
        return when(alignment) {
            Alignment.START -> 0
            Alignment.CENTER -> (cell.getCellWidth() - cell.getContentWidth() - (xMargin * 2))/2
            Alignment.END -> cell.getCellWidth() - cell.getContentWidth() - (xMargin * 2)
        }
    }

    abstract class Properties {
        abstract val propId: String
    }
}