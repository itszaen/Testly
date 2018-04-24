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

class DevChatUserData(id: String, displayName: String, mail: String, profileUrl: Uri) : FirebaseAuthUserData(){
    var displayName: String = ""
    init{
        FirebaseAuthUserData(id,mail,profileUrl)
        this.displayName = displayName
    }
}