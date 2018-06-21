package com.zaen.testly.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R

/**
 * Created by zaen on 2/27/18.
 */
class PastexamFragment : FileBrowserFragment() {
    companion object {
        const val bucket = "testly-2014.appspot.com"
        const val sub = "subdirectories"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_pastexam
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
