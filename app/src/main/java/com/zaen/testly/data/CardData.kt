package com.zaen.testly.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CardData(
        override var id : String,
        override var timestamp: Long,
        open var title: String,
        open var subject: String,
        open var cardType: String,
        open var hasAnswerCard: Boolean,
        open var question: String,
        open var answerText: String?) : FirebaseDocument(id, timestamp, CARD), Parcelable {
    companion object {
        // Card type identifier
        const val CARD_TYPE_SELECTION = "selection"
        const val CARD_TYPE_SELECTION_MULTIPLE = "multiple"
        const val CARD_TYPE_SELECTION_MULTIPLE_ORDERED = "ordered"
        const val CARD_TYPE_SPELLING = "spelling"
    }
}

open class SelectionCardData(
        override var id : String,
        override var timestamp: Long,
        override var title: String,
        override var subject: String,
        override var hasAnswerCard: Boolean,
        override var question: String,
        var options : ArrayList<String>,
        var answer: Int,
        override var answerText: String?
)
    : CardData(id, timestamp, title, subject, CARD_TYPE_SELECTION, hasAnswerCard, question, answerText) {
}

open class SelectionMultipleCardData(
        override var id: String,
        override var timestamp: Long,
        override var title: String,
        override var subject: String,
        override var hasAnswerCard: Boolean,
        override var question: String,
        open var options: ArrayList<String>,
        var answerList: ArrayList<Int>,
        override var answerText: String?
)
    : CardData(id, timestamp, title, subject, CARD_TYPE_SELECTION_MULTIPLE, hasAnswerCard, question, answerText) {
}

class SelectionMultipleOrderedCardData(
        override var id: String,
        override var timestamp: Long,
        override var title: String,
        override var subject: String,
        override var hasAnswerCard: Boolean,
        override var question: String,
        override var options: ArrayList<String>,
        val orderedAnswers: ArrayList<Int>,
        override var answerText: String?
)
    : SelectionMultipleCardData(id, timestamp, title, subject, hasAnswerCard, question, options, orderedAnswers, answerText){
    override var cardType = CARD_TYPE_SELECTION_MULTIPLE_ORDERED
}

class SpellingCardData(
        override var id : String,
        override var timestamp: Long,
        override var title: String,
        override var subject: String,
        override var hasAnswerCard: Boolean,
        override var question: String,
        var mask: ArrayList<Int>,
        var answerMasks: ArrayList<Int>,
        override var answerText: String?
)
    : CardData(id, timestamp, title, subject, CARD_TYPE_SPELLING, hasAnswerCard, question, answerText) {

}