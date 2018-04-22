package com.zaen.testly.data

import android.net.Uri


data class DevChatMessageData(
        val message:String,
        val sender: DevChatUserData,
        val createdAt: Long
)
data class DevChatUserData(
        val userId: String,
        val displayName:String,
        val profileUrl: Uri?
)