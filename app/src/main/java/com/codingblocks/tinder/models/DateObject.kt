package com.codingblocks.tinder.models

data class DateObject(val date: String) : ListObject() {
    override fun getType(userId: String): Int = TYPE_DATE
}