package com.zaen.testly

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.zaen.testly.data.CardData
import com.zaen.testly.data.CasData
import com.zaen.testly.data.SetData
import com.zaen.testly.utils.Common
import com.zaen.testly.utils.LogUtils

/*
* DO NOT SAVE DOCUMENT WHEN NOT VALID
*
*
* */

class CreateCasData (val context: Any) {
    companion object {
    }

    var registrationCard: ListenerRegistration? = null
    var registrationSet: ListenerRegistration? = null

    var casList = arrayListOf<CasData>()
    var storedCasDocuments = arrayListOf<DocumentSnapshot>()
    var cardList = arrayListOf<CardData>()
    var storedCardDocuments = arrayListOf<DocumentSnapshot>()
    var setList = arrayListOf<SetData>()
    var storedSetDocuments = arrayListOf<DocumentSnapshot>()

    var cardCollectionRef = FirebaseFirestore.getInstance().collection("cards")
    var setCollectionRef = FirebaseFirestore.getInstance().collection("sets")

    interface CreateCasDataListener{
        fun onCasData()
    }

    fun listenToCard(listener: CreateCasDataListener){
        TestlyFirestore(this).addCollectionListener(cardCollectionRef.orderBy("timestamp"),object: TestlyFirestore.CollectionChangeListener{
            override fun handleListener(listener: ListenerRegistration?) {
                registrationCard = listener
            }

            override fun onFailure(exception: Exception?) {
                registrationCard = null
            }

            override fun onNewDocument(path: Query, snapshot: DocumentSnapshot) {
                saveCas(snapshot)
                listener.onCasData()
            }

            override fun onModifyDocument(path: Query, snapshot: DocumentSnapshot) {
                val card = getCardFromDocument(snapshot)
                if (card != null){
                    storedCasDocuments[storedCasDocuments.indexOf(snapshot)] = snapshot
                    casList[storedCasDocuments.indexOf(snapshot)] = card
                    storedCardDocuments[storedCardDocuments.indexOf(snapshot)] = snapshot
                    cardList[(storedCardDocuments.indexOf(snapshot))] = card
                } else {
                    // remove if it was previously valid
                    if (storedCasDocuments.contains(snapshot)){
                        casList.removeAt(storedCasDocuments.indexOf(snapshot))
                    }
                    if (storedCardDocuments.contains(snapshot)){
                        cardList.removeAt(storedCardDocuments.indexOf(snapshot))
                    }
                    storedCasDocuments.remove(snapshot)
                    storedCardDocuments.remove(snapshot)
                }
                listener.onCasData()

            }

            override fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot) {
                val card = getCardFromDocument(snapshot)
                if (card != null) {
                    casList.removeAt(storedCasDocuments.indexOf(snapshot))
                    cardList.removeAt(storedCardDocuments.indexOf(snapshot))
                }
                storedCardDocuments.remove(snapshot)
                storedCasDocuments.remove(snapshot)
                listener.onCasData()
            }
        })
    }

    fun listenToSet(listener: CreateCasDataListener){
        TestlyFirestore(this).addCollectionListener(setCollectionRef.orderBy("timestamp"), object: TestlyFirestore.CollectionChangeListener {
            override fun handleListener(listener: ListenerRegistration?) {
                registrationSet = listener
            }

            override fun onFailure(exception: Exception?) {
                registrationSet = null
            }

            override fun onNewDocument(path: Query, snapshot: DocumentSnapshot) {
            }

            override fun onModifyDocument(path: Query, snapshot: DocumentSnapshot) {
            }

            override fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot) {
            }
        })
    }

    fun saveCas(snapshot: DocumentSnapshot){
        when (checkType(snapshot)){
            CasData.card -> {
                saveCard(snapshot)
            }
            CasData.set -> {

            }
            else -> {
                // invalid data
                Log.w(LogUtils.TAG(this),"[Failure] Invalid cas data. Id:${snapshot.id}")
            }
        }
    }

    fun saveCard(snapshot: DocumentSnapshot){
        val card = getCardFromDocument(snapshot)
        if (card != null){
            storedCasDocuments.add(snapshot)
            storedCardDocuments.add(snapshot)
            casList.add(card)
            cardList.add(card)
        }
    }

    fun checkType(snapshot: DocumentSnapshot):String?{
        return when (snapshot.get("casType") as String){
            CasData.card -> CasData.card
            CasData.set -> CasData.set
            else -> null
        }
    }

    fun saveSet(snapshot: DocumentSnapshot){

    }

    fun getCardFromDocument(snapshot: DocumentSnapshot):CardData?{
        if (Common().allNotNull(
                        snapshot.get("id"),
                        snapshot.get("timestamp"),
                        snapshot.get("title"),
                        snapshot.get("subject"),
                        snapshot.get("hasAnswerCard"),
                        snapshot.get("question")
                )){
            Log.w(LogUtils.TAG(this),"Invalid card data. Id:${snapshot.id}")
            return null
        }
        return CardData(
                snapshot.get("id") as String,
                snapshot.get("timestamp") as Long,
                snapshot.get("title") as String,
                snapshot.get("subject") as String,
                snapshot.get("hasAnswerCard") as Boolean,
                snapshot.get("question") as String,
                snapshot.get("answerText") as String?
        )
    }

    fun getSetFromDocument(snapshot: DocumentSnapshot){

    }

}