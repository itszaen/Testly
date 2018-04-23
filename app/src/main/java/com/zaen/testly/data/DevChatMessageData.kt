package com.zaen.testly.data

import android.net.Uri


class DevChatMessageData (message: String, sender: DevChatUserData, createdAt: Long){
    var message: String? = null
    var sender: DevChatUserData? = null
    var createdAt: Long? = null
    init {
        this.message = message
        this.sender = sender
        this.createdAt = createdAt
    }
}

class DevChatUserData : FirebaseAuthUserData(){
    var displayName: String = ""
    fun DevChatUserData(id: String, mail: String, profileUrl: Uri, displayName: String){
        FirebaseAuthUserData(id,mail,profileUrl)
        this.displayName = displayName
    }
}