package com.zaen.testly.data

import com.zaen.testly.R.mipmap.email
import java.sql.Timestamp

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


}