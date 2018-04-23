package com.zaen.testly.data

open class SetData {
    var id: String = ""
    // Unique document id

    var casType: String = "set"
    // card, *set

    var type: String = ""
    // check, test

    var cardType: String = ""
    // mixed, vocabulary, spelling

    var subjectType: String = ""
    // mixed, english, math, japanese...

    var cards: ArrayList<String> = arrayListOf()
    // List of cards to look for

    fun SetData(id: String, casType: String, type: String, cardType: String, subjectType: String, cards: ArrayList<String>){
        this.id = id
        this.casType = casType
        this.type = type
        this.cardType = cardType
        this.subjectType = subjectType
        this.cards = cards
    }
}