package com.zaen.testly.intro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment

/**
 * Created by zaen on 3/11/18.
 */
class FaqFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_intro_faq
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}