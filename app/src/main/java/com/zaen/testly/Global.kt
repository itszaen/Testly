package com.zaen.testly

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class Global(){
    companion object {
        var userinfoRef: DocumentReference? = null
    }
}