package com.wwdablu.soumya.simplypdfsample

object JSONStruct {

    const val payload = """
        {
            "page" : [
                {
                    "type" : "setup",
                    "margin" : {
                        "start" : 125,
                        "top" : 125,
                        "end" : 125,
                        "bottom" : 125
                    },
                    "backgroundcolor" : "#C8C8C8"
                },
                {
                    "type" : "header",
                    "contents" : [
                        {
                            "type" : "text",
                            "content" : "Simplypdf",
                            "properties" : {
                                "size" : 24,
                                "color" : "#000000",
                                "underline" : false,
                                "strikethrough" : false,
                                "alignment" : "ALIGN_CENTER"
                            }
                        },
                        {
                            "type" : "text",
                            "content" : "Version 2.0",
                            "properties" : {
                                "size" : 20,
                                "color" : "#000000",
                                "underline" : true,
                                "strikethrough" : false,
                                "alignment" : "ALIGN_CENTER"
                            }
                        }
                    ]
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
                    "format" : "url"
                },
                {
                    "type" : "text",
                    "content" : "Source code published in GitHub",
                    "properties" : {
                        "size" : 20,
                        "color" : "#000000",
                        "underline" : true,
                        "strikethrough" : false
                    }
                },
                {
                    "type" : "not_supported_currently",
                    "source" : "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",
                    "format" : "base64"
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
                                    "width" : 100,
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