package com.zaen.testly.activities.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.auth.SignupUserinfo
import com.zaen.testly.utils.InformUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.form_signup_userinfo.*

/**
 * Created by zaen on 3/22/18.
 */

class SignupInfoActivity : BaseActivity(), SignupUserinfo.ExceptionHandler, SignupUserinfo.SuccessListener {
    companion object {
    }
    var userinfo: SignupUserinfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_signup_info
        super.onCreate(savedInstanceState)
        userinfo = SignupUserinfo(this,input_username,edit_username,input_fullname_last,edit_fullname_last,input_fullname_first,edit_fullname_first,spinner_signup_school,spinner_signup_grade,spinner_signup_class, error_signup_school_required,error_signup_grade_required,error_signup_class_required)
                .build()
    }

    fun onSignUp(view: View) {
        if (userinfo!!.checkInfoField()){
            userinfo!!.registerUserInfo()
        }
    }
    override fun onException(error: String, e: Exception){
        InformUtils(this).snackyException(error,e)
    }
    override fun onSuccess() {
        Toasty.success(this,"User information registered!",Toast.LENGTH_SHORT,true).show()
        setResult(RESULT_OK,intent)
        finish()
    }
}