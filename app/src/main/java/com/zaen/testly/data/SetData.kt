package com.zaen.testly.data

import com.google.firebase.firestore.DocumentSnapshot

open class SetData(
        override var id: String,
        override var timestamp: Long,
        open var title: String,
        open var setType: String,
        open var cardType: String,
        open var subjectType: String,
        open var cards: ArrayList<String>
): FirebaseDocument(id,timestamp,SET){
    companion object {
        const val SET_TYPE_CHECK = "check"
        const val SET_TYPE_TEST = "test"
        const val SET_CARD_TYPE_MIXED = "mixed"
        const val SET_CARD_TYPE_SELECTION = "selection"
        const val SET_CARD_TYPE_SPELLING = "spelling"
        const val SET_SUBJECT_TYPE_MIXED = "mixed"
        const val SET_SUBJECT_TYPE_SINGLE = "single"

        fun getSetDataFromDocument(snapshot: DocumentSnapshot) : SetData{
            return SetData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("title") as String,
                    snapshot.get("setType") as String,
                    snapshot.get("cardType") as String,
                    snapshot.get("subjectType") as String,
                    snapshot.get("cards") as ArrayList<String>
                    )
        }
    }
}