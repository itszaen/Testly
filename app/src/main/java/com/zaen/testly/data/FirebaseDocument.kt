package com.zaen.testly.data

import com.twitter.sdk.android.core.models.User

open class FirebaseDocument(
        open var id: String,
        open var timestamp: Long,
        open var type: String
) {
    companion object {
        // document type identifier
        const val CARD = "card"
        const val SET = "set"
        const val CHAT = "chat"
        const val FEEDBACK = "feedback"
        const val SCHOOL = "school"
        const val GRADE = "grade"
        const val CLASS = "class"
        const val PATH = "storage"
        const val USER = "user"
    }
}