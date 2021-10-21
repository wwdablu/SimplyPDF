package com.wwdablu.soumya.simplypdf.document

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

abstract class PageModifier {
    internal abstract fun render(simplyPdfDocument: SimplyPdfDocument)
}