package com.zaen.testly.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CardData(
        override var id : String,
        override var timestamp: Long,
        open var title: String,
        open var subject: String,
        open var hasAnswerCard: Boolean,
        open var question: String,
        open var answerText: String?) : CasData(id, timestamp), Parcelable {
    open var type = ""
    override var casType = CasData.card

//    var title: String = ""
//    // Unique name
//
//    open var type: String = ""
//    // unsorted, selection, multiple, ordered, spelling,
//
//    var subject: String = ""
//    // unsorted, english, math, japanese...
//
//    var question: String = ""
//    // question
//
//    var hasAnswerCard: Boolean = false
//    // Whether to have a separate answer card
//
//    var answerText: String? = null
//    // If has answer card, content

//    fun CardData(id : String, timestamp: Long, title: String, type: String, subject: String, hasAnswerCard: Boolean,
//                 question: String, answerText: String?) {
//        CasData(id,timestamp)
//        this.title = title
//        this.type = type
//        this.subject = subject
//        this.question = question
//        this.hasAnswerCard = hasAnswerCard
//        this.answerText = answerText
//    }
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
    : CardData(id, timestamp, title, subject, hasAnswerCard, question, answerText) {
    override var type = "selection"

//    var options: ArrayList<String> = ArrayList<String>()
//
//    open var answer: Int = 0

//    init {
//        this.options.addAll(options)
//        this.answer = answer
//    }
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
    : CardData(id, timestamp, title, subject, hasAnswerCard, question, answerText) {
    override var type = "multiple"

//    var options: ArrayList<String> = ArrayList<String>()
//
//    var answerList: ArrayList<Int> = ArrayList<Int>()
//
//    init {
//        CardData(id, timestamp, title, type, subject, hasAnswerCard, question, answerText)
//        this.options.addAll(options)
//        this.answerList.addAll(answers)
//    }
}

class SelectionMultipleOrderedCardData(
        override var id: String,
        override var timestamp: Long,
        override var title: String,
        override var subject: String,
        override var hasAnswerCard: Boolean,
        override var question: String,
        override var options: ArrayList<String>,
        var orderedAnswers: ArrayList<Int>,
        override var answerText: String?
)
    : SelectionMultipleCardData(id, timestamp, title, subject, hasAnswerCard, question, options, orderedAnswers, answerText){
    override var type = "ordered"
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
    : CardData(id, timestamp, title, subject, hasAnswerCard, question, answerText) {
    override var type = "spelling"

//    var mask: ArrayList<Int> = ArrayList<Int>()
//
//    var answerMasks: ArrayList<Int> = ArrayList<Int>()

    fun SpellingCardData(id : String, timestamp: Long, title: String, subject: String, hasAnswerCard: Boolean,
                         question: String, mask: ArrayList<Int>, answerMasks: ArrayList<Int>, answerText: String?) {
//        CardData(id, timestamp, title, subject, hasAnswerCard, question, answerText)
//        this.mask = mask
//        this.answerMasks.addAll(answerMasks)
    }

}