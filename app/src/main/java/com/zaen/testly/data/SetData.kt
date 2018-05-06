package com.zaen.testly.data

open class SetData(
        id: String,
        timestamp: Long,
        var title: String,
        var type: String,
        var cardType: String,
        var subjectType: String,
        var cards: ArrayList<String>) : CasData(id, timestamp) {
    override var casType = CasData.set

//    var title: String = ""
//
//    var type: String = ""
//    // check, test
//
//    var cardType: String = ""
//    // mixed, vocabulary, spelling
//
//    var subjectType: String = ""
//    // mixed, english, math, japanese...
//
//    var cards: ArrayList<String> = arrayListOf()
//    // List of cards to look for

//    fun SetData(id: String, timestamp: Long, title: String, type: String, cardType: String, subjectType: String, cards: ArrayList<String>){
//        CasData(id, timestamp)
//        this.title = title
//        this.type = type
//        this.cardType = cardType
//        this.subjectType = subjectType
//        this.cards = cards
//    }
}