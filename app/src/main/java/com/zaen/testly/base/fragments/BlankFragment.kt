package com.zaen.testly.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R

class BlankFragment : BaseFragment() {
    companion object {
        fun newInstance() : BlankFragment {
            return BlankFragment()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.blank_fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}