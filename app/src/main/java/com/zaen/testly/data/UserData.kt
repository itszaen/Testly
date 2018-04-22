package com.zaen.testly.data

import android.net.Uri

/**
 * Created by zaen on 2/21/18.
 */
data class UserData(
        //
        val isAdmin: Boolean,
        val isDeveloper: Boolean,
        val isProvider: Boolean,

        val userId: Int,
        val email: String,
        val name: String,
        val displayName: String,
        val profileUrl: Uri,

        // User
        val school: String,
        val grade: Int,
        val class_: Int
)