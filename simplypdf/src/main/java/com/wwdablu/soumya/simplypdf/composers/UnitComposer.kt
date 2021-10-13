package com.wwdablu.soumya.simplypdf.composers

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

abstract class UnitComposer(simplyPdfDocument: SimplyPdfDocument) : Composer(simplyPdfDocument) {

    abstract class Properties {
        abstract val propId: String
    }
}