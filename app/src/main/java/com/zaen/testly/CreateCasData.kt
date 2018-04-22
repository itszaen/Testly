package com.zaen.testly

import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.data.SetData
import com.zaen.testly.data.SpellingCardData

class Cas{
    val card: ArrayList<SpellingCardData?>? = null
    val set: ArrayList<SetData?>? = null
}

class CreateCasData (context: Any) {
    companion object {
        const val TAG = "CreateCasData"
    }
    private var mListener: CreateCasData? = null

    var casList = (cardList(),setList())
    var cardList = arrayListOf<SpellingCardData?>()
    var setList = arrayListOf<SetData?>()
    var latestDataTime : Long = 0
    var isListening = false
    var storedMessages = arrayListOf<String>()

    var cardCollection = FirebaseFirestore.getInstance().collection("cards")
    var setCollection = FirebaseFirestore.getInstance().collection("sets")

    interface CreateCasDataListener{
        fun onGotCasData()
    }

}