package com.wwdablu.soumya.simplypdfsample

object JSONStruct {

    const val payload = """
        {
            "page" : [
            {
                "type" : "world"
            },
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
                    "source" : "https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4"
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
                    "type" : "newpage"
                },
                {
                    "type" : "space",
                    "height" : 125
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
                        },
                        {
                            "row" : [
                                {
                                    "type" : "image",
                                    "type" : "image",
                                    "source" : "https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4"
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