package com.zaen.testly.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class SetData(
        override var id: String,
        override var timestamp: Long,
        open var title: String,
        open var setType: String,
        open var cardType: String,
        open var subjectType: String,
        open var cards: ArrayList<String>
): FirebaseDocument(id,timestamp,SET), Parcelable{
    companion object {
        const val SET_TYPE_CHECK = "check"
        const val SET_TYPE_TEST = "test"
        const val SET_CARD_TYPE_MIXED = "mixed"
        const val SET_CARD_TYPE_SELECTION = "selection"
        const val SET_CARD_TYPE_SPELLING = "spelling"
        const val SET_SUBJECT_TYPE_MIXED = "mixed"
        const val SET_SUBJECT_TYPE_SINGLE = "single"
    }
}