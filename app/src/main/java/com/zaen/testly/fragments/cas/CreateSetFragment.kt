package com.zaen.testly.fragments.cas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.data.SetData.Companion.SET_CARD_TYPE_MIXED
import com.zaen.testly.data.SetData.Companion.SET_SUBJECT_TYPE_MIXED
import com.zaen.testly.data.SetData.Companion.SET_TYPE_CHECK
import com.zaen.testly.fragments.base.BaseFragment

class CreateSetFragment  : BaseFragment(){
    companion object {
    }

    interface SubmitSetListener{
        fun onSubmitSetSuccessful()
    }
    private var mListener: SubmitSetListener? = null

    private var setType = SET_TYPE_CHECK

    private var setCardType = SET_CARD_TYPE_MIXED

    private var setSubjectType = SET_SUBJECT_TYPE_MIXED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SubmitSetListener){
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_create_set
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            return
        }
    }

    private fun updateUI(){

    }
}