package com.zaen.testly.cas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.fragments.base.BaseFragment

class CreateCasPageFragment : BaseFragment(){
    companion object {
        fun newInstance(): CreateCasPageFragment {
            return CreateCasPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}