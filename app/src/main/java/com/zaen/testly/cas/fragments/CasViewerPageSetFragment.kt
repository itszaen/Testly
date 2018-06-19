package com.zaen.testly.cas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.fragments.base.BaseFragment

class CasViewerPageSetFragment : BaseFragment() {
    companion object {
        fun newInstance(set: SetData): CasViewerPageSetFragment {
            val frag = CasViewerPageSetFragment()
            val bundle = Bundle()
            bundle.putParcelable(FirebaseDocument.CARD,set)
            frag.arguments = bundle
            return frag
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_cas_viewer_page_set
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}