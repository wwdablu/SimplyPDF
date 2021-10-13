package com.wwdablu.soumya.simplypdf.jsonengine

internal interface Node {
    companion object {
        const val CONTENT = "contents"
        const val TYPE = "type"
        const val TYPE_PROPERTIES = "properties"
        const val TYPE_TEXT = "text"
        const val TYPE_IMAGE = "image"
        const val TYPE_TABLE = "table"
        const val TYPE_SHAPE = "shape"
        const val TYPE_SPACE = "space"
        const val TYPE_NEWPAGE = "newpage"
        const val COMPOSER_TEXT_CONTENT = "content"
        const val COMPOSER_IMAGE_URL = "imageurl"
        const val COMPOSER_TABLE_CONTENTS = "contents"
        const val COMPOSER_TABLE_ROW = "row"
        const val COMPOSER_TABLE_WIDTH = "width"
        const val COMPOSER_SHAPE_SHAPE = "shape"
        const val COMPOSER_SHAPE_RADIUS = "radius"
        const val COMPOSER_SHAPE_WIDTH = "width"
        const val COMPOSER_SHAPE_HEIGHT = "height"
        const val COMPOSER_SHAPE_POINTS = "points"
        const val COMPOSER_SHAPE_LINE = "line"
        const val COMPOSER_SPACE_HEIGHT = "height"
    }
}