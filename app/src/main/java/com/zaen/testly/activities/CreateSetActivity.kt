package com.zaen.testly.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.util.AttributeSet
import android.view.View
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.fragments.cas.CreateCardFragment
import kotlinx.android.synthetic.main.activity_create_card.*

class CreateSetActivity : BaseActivity(),
    CreateCardFragment.FragmentClickListener{
    var toolbar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_set
        super.onCreate(savedInstanceState)

        toolbar = this.supportActionBar

        if (fragment_container_create_card != null){
            if (savedInstanceState != null){
                return
            }
            val mainFragment = CreateCardFragment()
            mainFragment.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_create_card,mainFragment)
                    .commit()
        }
    }

    override fun onFragmentCalled(newFragment: Fragment, title: String) {
        val args = Bundle()
        args.putString("TITLE",title)
        newFragment.arguments = args
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_create_card,newFragment)
                .addToBackStack(null)
                .commit()
        toolbar?.title = title

    }


}
