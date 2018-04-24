package com.zaen.testly.data

open class SetData : CasData() {
    var title: String = ""

    var type: String = ""
    // check, test

    var cardType: String = ""
    // mixed, vocabulary, spelling

    var subjectType: String = ""
    // mixed, english, math, japanese...

    var cards: ArrayList<String> = arrayListOf()
    // List of cards to look for

    fun SetData(id: String, title: String, type: String, cardType: String, subjectType: String, cards: ArrayList<String>){
        CasData(id)
        this.title = title
        this.type = type
        this.cardType = cardType
        this.subjectType = subjectType
        this.cards = cards
    }
}