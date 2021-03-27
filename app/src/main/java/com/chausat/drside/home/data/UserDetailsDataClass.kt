package com.chausat.drside.home.data

data class UserDetailsDataClass(
    val userId: Int,
    val userName: String,
    val userContact: String,
    val appointmentTime: String,
    val gender: String,
    val isApproved: String = "pending"
) {
    constructor() : this(
        userId = 0,
        userName = "",
        userContact = "",
        appointmentTime = "",
        gender = "",
        isApproved = "pending"
    )
}