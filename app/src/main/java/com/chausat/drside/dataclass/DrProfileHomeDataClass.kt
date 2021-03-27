package com.chausat.drside.dataclass

data class DrProfileHomeDataClass(
    var profile_image: String,
    var name: String
){
    constructor():this("","")
}
