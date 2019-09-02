package com.codingblocks.tinder.models

class Matches(
    val uid: String,
    val name: String,
    val photo: String
){
    constructor() : this("","","")
}
