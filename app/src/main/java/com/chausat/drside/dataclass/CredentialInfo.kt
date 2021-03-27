package com.chausat.drside.dataclass

data class CredentialInfo(
    var phone_number: String,
    var password: String
){
    constructor():this("","")
}
