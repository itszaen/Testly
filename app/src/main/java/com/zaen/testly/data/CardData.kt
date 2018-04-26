package com.zaen.testly.data

open class CardData : CasData(){

    var language: String = ""
    // unsorted, english, japanese, chinese

    var title: String = ""
    // Unique name

    var type: String = ""
    // unsorted, *vocabulary, spelling,

    var subject: String = ""
    // unsorted, english, math, japanese...

    var hasAnswerCard: Boolean = false
    // Whether to have a separate answer card

    fun CardData(id : String, title: String, language : String, type: String, subject: String, hasAnswerCard: Boolean) {
        CasData(id)
        this.language = language
        this.title = title
        this.type = type
        this.subject = subject
        this.hasAnswerCard = hasAnswerCard
    }
}

class VocabularyCardData : CardData() {
    var question: String = ""

    var options: ArrayList<String> = ArrayList<String>()

    var answer: String = ""

    var returnType: Any? = null
    // boolean?

    fun VocabularyCardData(id : String, title: String, language : String, type: String, subject: String, hasAnswerCard: Boolean,
                           question: String, options : ArrayList<String>, answer: String, returnType: Any?) {
        CardData(id, title, language, type, subject, hasAnswerCard)
        this.question = question
        this.options.addAll(options)
        this.returnType = returnType
    }
}
class SpellingCardData : CardData() {
    var question: String = ""

    var mask: ArrayList<Int> = ArrayList<Int>()

    var answer: String = ""

    var returnType: Any? = null

    fun SpellingCardData(id : String, title: String, language : String, type: String, subject: String, hasAnswerCard: Boolean,
                         question: String, mask: ArrayList<Int>, answer: String, returnType: Any?) {
        CardData(id, title, language, type, subject, hasAnswerCard)
        this.question = question
        this.mask = mask
        this.answer = answer
        this.returnType = returnType
    }

}