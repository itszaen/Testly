package com.zaen.testly

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.utils.CommonUtils
import com.zaen.testly.utils.LogUtils

/*
* DO NOT SAVE DOCUMENT WHEN NOT VALID
* */

class CreateCasData (val context: Any) {
    companion object {
    }

    var registrationCard: ListenerRegistration? = null
    var registrationSet: ListenerRegistration? = null

    var casList = arrayListOf<FirebaseDocument>()
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

    fun createCasRequest(type: String, orderBy: String?, wheres: HashMap<String,Any?>?):Query?{
        var reference: Query?
        reference = when (type){
            FirebaseDocument.CARD -> cardCollectionRef
            FirebaseDocument.SET  -> setCollectionRef
            else -> null
        }
        if (orderBy != null){
            reference = reference?.orderBy(orderBy)
        }

        if (wheres != null) {
            for ((key: String, value: Any?) in wheres.iterator()){
                reference = reference?.whereEqualTo(key, value)
            }
        }

        return reference
    }

    fun listenToCard(query: Query,listener: CreateCasDataListener){
        TestlyFirestore(this).addCollectionListener(query,object: TestlyFirestore.CollectionChangeListener{
            override fun handleListener(registration: ListenerRegistration?) {
                registrationCard = registration
            }

            override fun onFailure(exception: Exception?) {
                registrationCard = null
            }

            override fun onNewDocument(path: Query, snapshot: DocumentSnapshot) {
                saveCas(snapshot)
                listener.onCasData()
            }

            override fun onModifyDocument(path: Query, snapshot: DocumentSnapshot) {
                val card = getCardDataFromDocument(snapshot)
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
                val card = getCardDataFromDocument(snapshot)
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
            override fun handleListener(registration: ListenerRegistration?) {
                registrationSet = registration
            }

            override fun onFailure(exception: Exception?) {
                registrationSet = null
            }

            override fun onNewDocument(path: Query, snapshot: DocumentSnapshot) {
                saveCas(snapshot)
                listener.onCasData()
            }

            override fun onModifyDocument(path: Query, snapshot: DocumentSnapshot) {
                val set = getSetDataFromDocument(snapshot)
                if (set != null){
                    storedCasDocuments[storedCasDocuments.indexOf(snapshot)] = snapshot
                    casList[storedCasDocuments.indexOf(snapshot)] = set
                    storedSetDocuments[storedSetDocuments.indexOf(snapshot)] = snapshot
                    setList[(storedSetDocuments.indexOf(snapshot))] = set
                } else {
                    // remove if it was previously valid
                    if (storedCasDocuments.contains(snapshot)){
                        casList.removeAt(storedCasDocuments.indexOf(snapshot))
                    }
                    if (storedSetDocuments.contains(snapshot)){
                        setList.removeAt(storedSetDocuments.indexOf(snapshot))
                    }
                    storedCasDocuments.remove(snapshot)
                    storedSetDocuments.remove(snapshot)
                }
                listener.onCasData()
            }

            override fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot) {
                val set = getCardDataFromDocument(snapshot)
                if (set != null) {
                    casList.removeAt(storedCasDocuments.indexOf(snapshot))
                    setList.removeAt(storedSetDocuments.indexOf(snapshot))
                }
                storedSetDocuments.remove(snapshot)
                storedCasDocuments.remove(snapshot)
                listener.onCasData()
            }
        })
    }

    fun saveCas(snapshot: DocumentSnapshot){
        when (checkType(snapshot)){
            FirebaseDocument.CARD -> {
                saveCard(snapshot)
            }
            FirebaseDocument.SET -> {
                saveSet(snapshot)
            }
            else -> {
                // invalid data
                Log.w(LogUtils.TAG(this),"[Failure] Invalid cas data. Id:${snapshot.id}")
            }
        }
    }

    fun saveCard(snapshot: DocumentSnapshot){
        val card = getCardDataFromDocument(snapshot)
        if (card != null){
            storedCasDocuments.add(snapshot)
            storedCardDocuments.add(snapshot)
            casList.add(card)
            cardList.add(card)
        }
    }

    fun saveSet(snapshot: DocumentSnapshot){
        val set = getSetDataFromDocument(snapshot)
        if (set != null){
            storedCasDocuments.add(snapshot)
            storedSetDocuments.add(snapshot)
            casList.add(set)
            setList.add(set)
        }
    }

    fun checkType(snapshot: DocumentSnapshot):String?{
        return when (snapshot.get("casType") as String){
            FirebaseDocument.CARD -> FirebaseDocument.CARD
            FirebaseDocument.SET -> FirebaseDocument.SET
            else -> null
        }
    }

    fun getCardDataFromDocument(snapshot: DocumentSnapshot):CardData?{
        if (CommonUtils().allNotNull(
                        snapshot.get("id"),
                        snapshot.get("timestamp"),
                        snapshot.get("title"),
                        snapshot.get("subject"),
                        snapshot.get("hasAnswerCard"),
                        snapshot.get("question")
                )){
            Log.w(LogUtils.TAG(this),"[Failure] Invalid CARD data. Id:${snapshot.id}")
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

    fun getSetDataFromDocument(snapshot: DocumentSnapshot):SetData?{
        if (CommonUtils().allNotNull(
                        snapshot.get("")
                )){
            Log.w(LogUtils.TAG(this),"[Failure] Invalid SET data. Id:${snapshot.id}")
            return null
        }
        return SetData.getSetDataFromDocument(snapshot)
    }

}