package com.chausat.drside.home.data

data class MagnetTherapyDataClass(
    val about_eng: String,
    val about_guj: String,
    val images: ArrayList<String>?,
    val why_eng: String,
    val why_guj: String
) {
    constructor() : this("", "",null , "", "")
}
