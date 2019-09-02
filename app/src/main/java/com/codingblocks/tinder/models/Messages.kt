package com.codingblocks.tinder.models

class Messages(
    var fuid: String,
    var message: String,
    var type: String,
    var from: String,
    var seen: Boolean = false,
    var time: Long = 0
) {

    constructor() : this(
        "", "", "", "", false, 0
    )
}