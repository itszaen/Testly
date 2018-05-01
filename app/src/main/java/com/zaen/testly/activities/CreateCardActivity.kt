package com.zaen.testly.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.MotionEvent
import com.zaen.testly.BuildConfig
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.fragments.cas.CreateCardFragment
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.SupportActivityDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.greenrobot.eventbus.util.ErrorDialogFragments

class CreateCardActivity : BaseActivity(){

    var mDelegate = SupportActivityDelegate(this)
    var toolbar : ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_card
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)

        toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        if (findFragment(CreateCardFragment::class.java) == null){
                    loadRootFragment(R.id.fragment_container_create_card,CreateCardFragment())
                }
    }


}
