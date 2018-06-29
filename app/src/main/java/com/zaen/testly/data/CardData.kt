package com.zaen.testly.data

import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

open class CardData(
        override var id : String,
        override var timestamp: Long,
        open var title: String,
        open var subject: String,
        open var cardType: String,
        open var hasAnswerCard: Boolean,
        open var question: String,
        open var answerText: String?) : FirebaseDocument(id, timestamp, CARD){
    companion object {
        // Card type identifier
        const val CARD_TYPE_SELECTION = "selection"
        const val CARD_TYPE_SELECTION_MULTIPLE = "multiple"
        const val CARD_TYPE_SELECTION_MULTIPLE_ORDERED = "ordered"
        const val CARD_TYPE_SPELLING = "spelling"
        fun getCardDataFromDocument(snapshot: DocumentSnapshot) : CardData?{
            return when (snapshot.get("cardType")){
                CardData.CARD_TYPE_SELECTION -> SelectionCardData.getSelectionCardDataFromDocument(snapshot)
                CardData.CARD_TYPE_SELECTION_MULTIPLE -> SelectionMultipleCardData.getSelectionMultipleCardDataFromDocument(snapshot)
                CardData.CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> SelectionMultipleOrderedCardData.getSelectionMultipleOrderedCardDataFromDocument(snapshot)
                CardData.CARD_TYPE_SPELLING -> SpellingCardData.getSpellingCardData(snapshot)
                else -> null
            }
        }
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
    companion object {
        fun getSelectionCardDataFromDocument(snapshot: DocumentSnapshot) : CardData{
            return SelectionCardData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("title") as String,
                    snapshot.get("subject") as String,
                    snapshot.get("hasAnswerCard") as Boolean,
                    snapshot.get("question") as String,
                    snapshot.get("options") as ArrayList<String>,
                    (snapshot.get("answer") as Long).toInt(),
                    snapshot.get("answerText") as String?
            )
        }
    }
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
    companion object {
        fun getSelectionMultipleCardDataFromDocument(snapshot: DocumentSnapshot) : CardData{
            val longAnswerList = snapshot.get("answerList") as ArrayList<Long>
            val answerList = longAnswerList.map { it.toInt() }.toIntArray().toCollection(ArrayList())
            return SelectionMultipleCardData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("title") as String,
                    snapshot.get("subject") as String,
                    snapshot.get("hasAnswerCard") as Boolean,
                    snapshot.get("question") as String,
                    snapshot.get("options") as ArrayList<String>,
                    answerList,
                    snapshot.get("answerText") as String?
            )
        }
    }
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
    companion object {
        fun getSelectionMultipleOrderedCardDataFromDocument(snapshot: DocumentSnapshot) : CardData{
            val longOrderedAnswers = snapshot.get("orderedAnswers") as ArrayList<Long>
            val orderedAnswers = longOrderedAnswers.map { it.toInt() }.toIntArray().toCollection(ArrayList())
            return SelectionMultipleOrderedCardData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("title") as String,
                    snapshot.get("subject") as String,
                    snapshot.get("hasAnswerCard") as Boolean,
                    snapshot.get("question") as String,
                    snapshot.get("options") as ArrayList<String>,
                    orderedAnswers,
                    snapshot.get("answerText") as String?
            )
        }

    }
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
    companion object {
        fun getSpellingCardData(snapshot: DocumentSnapshot) : CardData{
            val longMask = snapshot.get("mask") as ArrayList<Long>
            val mask = longMask.map { it.toInt() }.toIntArray().toCollection(ArrayList())
            val longAnswerMasks = snapshot.get("answerMasks") as ArrayList<Long>
            val answerMasks = longAnswerMasks.map { it.toInt() }.toIntArray().toCollection(ArrayList())
            return SpellingCardData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("title") as String,
                    snapshot.get("subject") as String,
                    snapshot.get("hasAnswerCard") as Boolean,
                    snapshot.get("question") as String,
                    mask,
                    answerMasks,
                    snapshot.get("answerText") as String?
            )
        }

    }

}