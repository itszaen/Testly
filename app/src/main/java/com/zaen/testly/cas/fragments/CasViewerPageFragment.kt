package com.zaen.testly.cas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.views.CasCardSelectionView

class CasViewerPageFragment : BaseFragment(){
    companion object {
        private val CAS_TYPE = "type"
        fun newInstance(type: String): CasViewerPageFragment {
            val arg = Bundle()
            when (type){
                FirebaseDocument.CARD -> arg.putString(CAS_TYPE,FirebaseDocument.CARD)
                FirebaseDocument.SET -> arg.putString(CAS_TYPE,FirebaseDocument.SET)
            }
            val frag = CasViewerPageFragment()
            frag.arguments = arg
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_cas_viewer_page
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.get(CAS_TYPE)
        when (type){
            //!!
            FirebaseDocument.CARD ->
        }
    }
}