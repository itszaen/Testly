package com.zaen.testly.activities.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.zaen.testly.R
import com.zaen.testly.auth.SignupUserinfo
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.form_signup_userinfo.*

/**
 * Created by zaen on 3/22/18.
 */

class SignupInfoActivity : AppCompatActivity(), SignupUserinfo.ExceptionHandler, SignupUserinfo.SuccessListener {
    companion object {
        val TAG = "SignupInfoActivity"
    }
    var userinfo: SignupUserinfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_info)
        userinfo = SignupUserinfo(this,input_username,edit_username,input_fullname_last,edit_fullname_last,input_fullname_first,edit_fullname_first,spinner_signup_school,spinner_signup_grade,spinner_signup_class)
                .Build()
    }

    fun onSignUp(view: View) {
        if (userinfo!!.checkInfoField()){
            userinfo!!.registerUserInfo()
        }
    }
    override fun onExceptionSnacky(exception: Exception,logText:String,errorText:String){
        Log.w(Auth.TAG,logText)
        Snacky.builder()
                .setActivity(this)
                .setText(errorText)
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("OPEN")
                .setActionClickListener {
                    MaterialDialog.Builder(this)
                            .title("Exception")
                            .content(exception.toString())
                            .positiveText(R.string.react_positive)
                            .show()
                }
                .error()
                .show()
    }
    override fun onSuccess() {
        Toasty.success(this,"User information registered!",Toast.LENGTH_SHORT,true).show()
        setResult(RESULT_OK,intent)
        finish()
    }
}