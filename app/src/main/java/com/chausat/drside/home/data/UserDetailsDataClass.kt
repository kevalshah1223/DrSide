package com.chausat.drside.home.data

data class UserDetailsDataClass(
    val userId: Int,
    val userName: String,
    val userContact: String,
    val appointmentTime: String,
    val appointmentDate: String,
    val gender: String,
    val approved: String,
    val note: String
) {
    constructor() : this(
        userId = 0,
        userName = "",
        userContact = "",
        appointmentTime = "",
        appointmentDate = "",
        gender = "",
        approved = "",
        note = ""
    )
}