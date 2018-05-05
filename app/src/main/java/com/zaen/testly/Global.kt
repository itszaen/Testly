package com.zaen.testly

import com.google.firebase.firestore.DocumentReference

class Global(){
    companion object {
        var userinfoRef: DocumentReference? = null
    }
}