package com.zaen.testly.data


class DevChatMessageData (message: String, sender: FirebaseAuthUserData, createdAt: Long){
    var message: String? = null
    var sender: UserData? = null
    var createdAt: Long? = null
    init {
        this.message = message
        this.sender = sender
        this.createdAt = createdAt
    }
}