package com.wwdablu.soumya.simplypdf.jsonengine

internal interface Node {
    companion object {
        const val CONTENTS = "contents"
        const val PAGE = "page"

        const val TYPE = "type"
        const val TYPE_PROPERTIES = "properties"

        const val TYPE_PAGE_SETUP = "setup"

        const val TYPE_TEXT = "text"
        const val TYPE_IMAGE = "image"

        const val COMPOSER_TEXT_CONTENT = "content"

        const val COMPOSER_IMAGE_SOURCE = "source"

        const val COMPOSER_TABLE_CONTENTS = "contents"
        const val COMPOSER_TABLE_ROW = "row"
        const val COMPOSER_TABLE_WIDTH = "width"

        const val SPACE_HEIGHT = "height"
    }
}