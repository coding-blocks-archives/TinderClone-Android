package com.codingblocks.tinder.models

data class ChatModelObject(val messages: Messages) : ListObject() {
    override fun getType(userId: String): Int =
        if (messages.from == userId) {
            TYPE_GENERAL_RIGHT
        } else
            TYPE_GENERAL_LEFT
}