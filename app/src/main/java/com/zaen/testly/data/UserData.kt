package com.zaen.testly.data

/**
 * Created by zaen on 2/21/18.
 */
class UserData(
        id: String, email: String, profileUrl: String,
        isAdmin: Boolean, isDeveloper: Boolean, isProvider: Boolean,
        displayName: String,
        firstName: String, lastName: String,
        schoolId: String, schoolName: String,
        gradeId: String, grade: String,
        classId: String, class_: String)
    : FirebaseAuthUserData(){

    // Check User Privilege
    var isAdmin: Boolean = false
    var isDeveloper: Boolean = false
    var isProvider: Boolean = false

    // User
    var displayName: String = ""
    var firstName = ""
    var lastName = ""
    var schoolId: String = ""
    var schoolName: String = ""
    var gradeId: String = ""
    var grade: String = ""
    var classId: String = ""
    var class_: String = ""

    // Provider

    // Developer

    // Admin

    init {
        FirebaseAuthUserData(id,email,profileUrl)
        this.isAdmin = isAdmin
        this.isDeveloper = isDeveloper
        this.isProvider = isProvider
        this.displayName = displayName
        this.firstName = firstName
        this.lastName = lastName
        this.schoolId = schoolId
        this.schoolName = schoolName
        this.gradeId = gradeId
        this.grade = grade
        this.classId = classId
        this.class_ = class_
    }
}