package com.wwdablu.soumya.simplypdf.jsonengine;

interface Node {
    String CONTENT = "contents";

    String TYPE = "type";
    String TYPE_PROPERTIES = "properties";

    String TYPE_TEXT = "text";
    String TYPE_IMAGE = "image";
    String TYPE_TABLE = "table";
    String TYPE_SHAPE = "shape";
    String TYPE_SPACE = "space";
    String TYPE_NEWPAGE = "newpage";

    String COMPOSER_TEXT_CONTENT = "content";

    String COMPOSER_IMAGE_URL = "imageurl";

    String COMPOSER_TABLE_CONTENTS = "contents";
    String COMPOSER_TABLE_ROW = "row";
    String COMPOSER_TABLE_WIDTH = "width";

    String COMPOSER_SHAPE_SHAPE = "shape";
    String COMPOSER_SHAPE_RADIUS = "radius";
    String COMPOSER_SHAPE_WIDTH = "width";
    String COMPOSER_SHAPE_HEIGHT = "height";
    String COMPOSER_SHAPE_POINTS = "points";
    String COMPOSER_SHAPE_LINE = "line";

    String COMPOSER_SPACE_HEIGHT = "height";
}
