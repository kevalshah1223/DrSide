package com.chausat.drside.home.data

data class ProspectionServicesDataClass(
    val about_eng: String,
    val about_guj: String,
    val why_eng: String,
    val why_guj: String,
){
    constructor():this("","","","")
}
