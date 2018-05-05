package com.zaen.testly.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcelize
open class CardData : CasData(), Parcelable {
    var title: String = ""
    // Unique name

    open var type: String = ""
    // unsorted, selection, multiple, ordered, spelling,

    var subject: String = ""
    // unsorted, english, math, japanese...

    var question: String = ""
    // question

    var hasAnswerCard: Boolean = false
    // Whether to have a separate answer card

    var answerText: String? = null
    // If has answer card, content

    fun CardData(id : String, timestamp: Long, title: String, type: String, subject: String, hasAnswerCard: Boolean,
                 question: String, answerText: String?) {
        CasData(id,timestamp)
        this.title = title
        this.type = type
        this.subject = subject
        this.question = question
        this.hasAnswerCard = hasAnswerCard
        this.answerText = answerText
    }
}

open class SelectionCardData(id : String, timestamp: Long, title: String, subject: String, hasAnswerCard: Boolean,
                             question: String, options : ArrayList<String>, answer: Int, answerText: String?) : CardData() {
    override var type = "selection"

    var options: ArrayList<String> = ArrayList<String>()

    open var answer: Int = 0

    init {
        CardData(id, timestamp, title, type, subject, hasAnswerCard, question, answerText)
        this.options.addAll(options)
        this.answer = answer
    }
}

open class SelectionMultipleCardData(id: String, timestamp: Long, title: String, subject: String, hasAnswerCard: Boolean,
                                     question: String, options: ArrayList<String>, answers: ArrayList<Int>, answerText: String?) : CardData() {
    override var type = "multiple"

    var options: ArrayList<String> = ArrayList<String>()

    var answerList: ArrayList<Int> = ArrayList<Int>()

    init {
        CardData(id, timestamp, title, type, subject, hasAnswerCard, question, answerText)
        this.options.addAll(options)
        this.answerList.addAll(answers)
    }
}

class SelectionMultipleOrderedCardData(id: String, timestamp: Long, title: String, subject: String, hasAnswerCard: Boolean,
                                       question: String, options: ArrayList<String>, orderedAnswers: ArrayList<Int>, answerText: String?) : SelectionMultipleCardData(id, timestamp, title, subject, hasAnswerCard, question, options, orderedAnswers, answerText){
    override var type = "ordered"
    init {

    }
}

class SpellingCardData : CardData() {
    override var type = "spelling"

    var mask: ArrayList<Int> = ArrayList<Int>()

    var answerMasks: ArrayList<Int> = ArrayList<Int>()

    fun SpellingCardData(id : String, timestamp: Long, title: String, subject: String, hasAnswerCard: Boolean,
                         question: String, mask: ArrayList<Int>, answerMasks: ArrayList<Int>, answerText: String?) {
        CardData(id, timestamp, title, type, subject, hasAnswerCard, question, answerText)
        this.mask = mask
        this.answerMasks.addAll(answerMasks)
    }

}