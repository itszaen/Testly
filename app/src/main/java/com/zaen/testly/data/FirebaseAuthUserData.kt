package com.zaen.testly.data

import android.net.Uri
import java.net.URL

open class FirebaseAuthUserData{
    var id: String = ""
    var mail: String = ""
    var profileUrl: Uri = Uri.EMPTY
    fun FirebaseAuthUserData(id: String, mail: String, profileUrl: Uri){
        this.id = id
        this.mail = mail
        this.profileUrl = profileUrl
    }
}