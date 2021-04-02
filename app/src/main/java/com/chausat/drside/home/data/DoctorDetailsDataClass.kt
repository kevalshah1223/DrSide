package com.chausat.drside.home.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DoctorDetailsDataClass(
    var name: String,
    var contact_number: String,
    var email: String,
    var address: String,
    var profile_image: String
) : Parcelable {
    constructor() : this("", "", "", "", "")
}
