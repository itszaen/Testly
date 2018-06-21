package com.zaen.testly.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment

class AdminPanelFragment : BaseFragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_admin_panel
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}