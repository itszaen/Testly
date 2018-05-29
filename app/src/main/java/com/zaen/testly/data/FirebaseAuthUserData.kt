package com.zaen.testly.data

open class FirebaseAuthUserData(
        override var id: String,
        override var timestamp: Long,
        val email: String,
        val profileUrl: String) : FirebaseDocument(id,timestamp,USER){
}