package com.zaen.testly

import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.data.CasData

class CreateCasData (context: Any) {
    companion object {
    }
    private var mListener: CreateCasData? = null

    var casList = arrayListOf<CasData>()
    var latestCasTime : Long = 0
    var isListening = false
    var storedCas = arrayListOf<String>()

    var cardCollection = FirebaseFirestore.getInstance().collection("cards")
    var setCollection = FirebaseFirestore.getInstance().collection("sets")

    interface CreateCasDataListener{
        fun onGotCasData()
    }

}