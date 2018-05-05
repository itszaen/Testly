package com.zaen.testly.data


class DevChatMessageData (message: String, sender: DevChatUserData, timestamp: Long){
    var text: String? = null
    var sender: DevChatUserData? = null
    var timestamp: Long? = null
    init {
        this.text = message
        this.sender = sender
        this.timestamp = timestamp
    }
}

class DevChatUserData(id: String, displayName: String, mail: String, profileUrl: String) : FirebaseAuthUserData(){
    var displayName: String = ""
    init{
        FirebaseAuthUserData(id,mail,profileUrl)
        this.displayName = displayName
    }
}