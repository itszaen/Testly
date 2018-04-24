package com.zaen.testly.data

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zaen.testly.R

/**
 * Created by zaen on 2/21/18.
 */
class UserData : FirebaseAuthUserData(){

    //
    var isAdmin: Boolean = false
    var isDeveloper: Boolean = false
    var isProvider: Boolean = false

    var firstName = ""
    var lastName = ""

    // User
    var school: String = ""
    var grade: Int = 0
    var class_: String = ""

    fun UserData(
            id: String,
            mail: String,
            profileUrl: Uri,
            isAdmin: Boolean,
            isDeveloper: Boolean,
            isProvider: Boolean,
            firstName: String,
            lastName: String,
            school: String,
            grade: Int,
            class_: String
    ){
        FirebaseAuthUserData(id,mail,profileUrl)
        this.isAdmin = isAdmin
        this.isDeveloper = isDeveloper
        this.isProvider = isProvider
        this.firstName = firstName
        this.lastName = lastName
        this.school = school
        this.grade = grade
        this.class_ = class_
    }
}

class DeveloperData{
    var clearance: Int = 5
    fun DeveloperData(){

    }
}

class CreateSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_set)
    }
}