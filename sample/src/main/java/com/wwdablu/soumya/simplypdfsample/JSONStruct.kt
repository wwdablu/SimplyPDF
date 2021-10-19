package com.wwdablu.soumya.simplypdfsample

object JSONStruct {

    const val payload = """
        {
            "header" : [
                {
                    "type" : "text",
                    "content" : "Simplypdf",
                    "properties" : {
                        "size" : 24,
                        "color" : "#000000",
                        "underline" : true,
                        "strikethrough" : false
                    }
                },
                {
                    "type" : "text",
                    "content" : "Version 2.0",
                    "properties" : {
                        "size" : 20,
                        "color" : "#000000",
                        "underline" : true,
                        "strikethrough" : false
                    }
                }
            ],
            "contents" : [
                {
                    "type" : "text",
                    "content" : "Simplypdf developed by Soumya Kanti Kar",
                    "properties" : {
                        "size" : 24,
                        "color" : "#000000",
                        "underline" : true,
                        "strikethrough" : false
                    }
                },
                {
                    "type" : "image",
                    "source" : "https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4",
                    "format" : "base64|url"
                },
                {
                    "type" : "shape",
                    "shape" : "circle",
                    "radius" : 50,
                    "properties" : {
                        "linecolor" : "#000000",
                        "linewidth" : 1,
                        "shouldfill" : true
                    }
                },
                {
                    "type" : "shape",
                    "shape" : "box",
                    "width" : 50,
                    "height" : 50,
                    "properties" : {
                        "linecolor" : "#000000",
                        "linewidth" : 1,
                        "shouldfill" : true
                    }
                },
                {
                    "type" : "newpage"
                },
                {
                    "type" : "shape",
                    "shape" : "freeform",
                    "points" : [
                        {
                            "line" : [0,0]
                        },
                        {
                            "line" : [100, 0]
                        },
                        {
                            "line" : [100, 100]
                        },
                        {
                            "line" : [0, 0]
                        }
                    ],
                    "properties" : {
                        "linecolor" : "#000000",
                        "linewidth" : 1,
                        "shouldfill" : true
                    }
                },
                {
                    "type" : "space",
                    "height" : 25
                },
                {
                    "type" : "table",
                    "contents" : [
                        {
                            "row" : [
                                {
                                    "type" : "text",
                                    "content" : "Version",
                                    "width" : 50,
                                    "properties" : {
                                        "size" : 24,
                                        "color" : "#000000",
                                        "underline" : true,
                                        "strikethrough" : false
                                    }
                                },
                                {
                                    "type" : "text",
                                    "content" : "2.0.0",
                                    "width" : 50,
                                    "properties" : {
                                        "size" : 24,
                                        "color" : "#000000",
                                        "underline" : true,
                                        "strikethrough" : false
                                    }
                                }
                            ]
                        },
                        {
                            "row" : [
                                {
                                    "type" : "text",
                                    "content" : "Source Code Available in GitHub",
                                    "width" : 50,
                                    "properties" : {
                                        "size" : 24,
                                        "color" : "#000000",
                                        "underline" : true,
                                        "strikethrough" : false
                                    }
                                }
                            ]
                        }
                    ],
                    "properties" : {
                        "width" : 1,
                        "color" : "#000000"
                    }
                }
            ]
        }
    """
}