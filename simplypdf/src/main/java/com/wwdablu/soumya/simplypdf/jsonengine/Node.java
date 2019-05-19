package com.wwdablu.soumya.simplypdf.jsonengine;

interface Node {
    String CONTENT = "contents";

    String TYPE = "type";
    String TYPE_PROPERTIES = "properties";

    String TYPE_TEXT = "text";
    String TYPE_IMAGE = "image";
    String TYPE_TABLE = "table";

    String COMPOSER_TEXT_CONTENT = "content";

    String COMPOSER_IMAGE_URL = "imageurl";

    String COMPOSER_TABLE_CONTENTS = "contents";
    String COMPOSER_TABLE_ROW = "row";
    String COMPOSER_TABLE_WIDTH = "width";
}
