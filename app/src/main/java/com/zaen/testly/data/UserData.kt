package com.zaen.testly.data

import android.databinding.BaseObservable

/**
 * Created by zaen on 2/21/18.
 */
class UserData() : BaseObservable() {
    var id: Int? = null
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var nickname: String? = null

}