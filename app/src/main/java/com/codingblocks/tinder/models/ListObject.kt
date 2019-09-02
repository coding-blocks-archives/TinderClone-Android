package com.codingblocks.tinder.models

abstract class ListObject {
    abstract fun getType(userId: String): Int

    companion object {
        val TYPE_DATE = 0
        val TYPE_GENERAL_RIGHT = 1
        val TYPE_GENERAL_LEFT = 2
    }
}



