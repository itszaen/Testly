package com.zaen.testly

import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.data.CardData
import com.zaen.testly.data.Cas
import com.zaen.testly.data.SetData
import com.zaen.testly.data.SpellingCardData

class Cas(cardList: ArrayList<CardData?>?, setList: ArrayList<SetData?>?) {
    var cardList: ArrayList<CardData?>? = null
    var setList: ArrayList<SetData?>? = null

    init {
        this.cardList = cardList
        this.setList = setList

    }
}

class CreateCasData (context: Any) {
    companion object {
        const val TAG = "CreateCasData"
    }
    private var mListener: CreateCasData? = null

    var cardList = arrayListOf<CardData?>()
    var setList = arrayListOf<SetData?>()
    var casList = Cas(cardList,setList)
    var latestDataTime : Long = 0
    var isListening = false
    var storedMessages = arrayListOf<String>()

    var cardCollection = FirebaseFirestore.getInstance().collection("cards")
    var setCollection = FirebaseFirestore.getInstance().collection("sets")

    interface CreateCasDataListener{
        fun onGotCasData()
    }

}