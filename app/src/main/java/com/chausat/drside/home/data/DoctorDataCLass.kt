package com.chausat.drside.home.data

data class DoctorDataCLass(
    val profile_image: String,
    val name: String,
    val contact_number: String,
    val email: String,
    val address: String,
    val about_eng: String,
    val about_gujju: String,
    val magnet_image: String,
    val clinic_name: String
){
    constructor():this("","","","","","","","","")
}
