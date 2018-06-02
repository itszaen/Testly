package com.zaen.testly.data

import com.google.firebase.firestore.DocumentSnapshot

/**
 * Created by zaen on 2/21/18.
 */
class UserData(
        override var id: String,
        override var timestamp: Long,
        val email: String,
        val profileUrl: String,
        val isAdmin: Boolean,
        val isDeveloper: Boolean,
        val isProvider: Boolean,
        val displayName: String,
        val firstName: String,
        val lastName: String,
        val schoolId: String,
        val schoolName: String,
        val gradeId: String,
        val grade: String,
        val classId: String,
        val class_: String)
    : FirebaseDocument(id, timestamp, USER){
    companion object {
        fun getUserDataFromDocument(snapshot: DocumentSnapshot) : UserData{
            return UserData(
                    snapshot.get("id") as String,
                    snapshot.get("timestamp") as Long,
                    snapshot.get("email") as String,
                    snapshot.get("profileUrl") as String,
                    snapshot.get("isAdmin") as Boolean,
                    snapshot.get("isDeveloper") as Boolean,
                    snapshot.get("isProvider") as Boolean,
                    snapshot.get("displayName") as String,
                    snapshot.get("firstName") as String,
                    snapshot.get("lastName") as String,
                    snapshot.get("schoolId") as String,
                    snapshot.get("schoolName") as String,
                    snapshot.get("gradeId") as String,
                    snapshot.get("grade") as String,
                    snapshot.get("classId") as String,
                    snapshot.get("class_") as String
            )
        }
        fun getUserDataFromHashMap(hashMap: HashMap<String,Any?>) : UserData{
            return UserData(
                    hashMap["id"] as String,
                    hashMap["timestamp"] as Long,
                    hashMap["email"] as String,
                    hashMap["profileUrl"] as String,
//                            snapshot.get("isAdmin") as Boolean,
//                            snapshot.get("isDeveloper") as Boolean,
//                            snapshot.get("isProvider") as Boolean,
                    hashMap["admin"] as Boolean,
                    hashMap["developer"] as Boolean,
                    hashMap["provider"] as Boolean,
                    hashMap["displayName"] as String,
                    hashMap["firstName"] as String,
                    hashMap["lastName"] as String,
                    hashMap["schoolId"] as String,
                    hashMap["schoolName"] as String,
                    hashMap["gradeId"] as String,
                    hashMap["grade"] as String,
                    hashMap["classId"] as String,
                    hashMap["class_"] as String
            )
        }
    }


}