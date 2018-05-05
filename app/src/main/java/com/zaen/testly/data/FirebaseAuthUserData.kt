package com.zaen.testly.data

open class FirebaseAuthUserData{
    var id: String = ""
    var email: String? = ""
    var profileUrl: String = ""
    fun FirebaseAuthUserData(id: String, email: String, profileUrl: String){
        this.id = id
        this.email = email
        this.profileUrl = profileUrl
    }
}