package com.zaen.testly.cas.children.createPool.fragments

import android.content.Context
import android.os.Bundle
import com.zaen.testly.base.fragments.BaseFragment

class CreatePoolFragment : BaseFragment(){
    interface SubmitPoolListener{
        fun onSubmitPoolSuccessful()
    }

    private var mListener: SubmitPoolListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CreatePoolFragment.SubmitPoolListener){
            mListener = context
        }
    }

}
