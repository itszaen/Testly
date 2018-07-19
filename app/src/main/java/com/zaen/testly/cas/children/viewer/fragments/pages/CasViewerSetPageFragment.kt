package com.zaen.testly.cas.children.viewer.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData

class CasViewerSetPageFragment : BaseFragment() {
    companion object {
        fun newInstance(set: SetData): CasViewerSetPageFragment {
            val frag = CasViewerSetPageFragment()
            val bundle = Bundle()
            bundle.putParcelable(FirebaseDocument.CARD,set)
            frag.arguments = bundle
            return frag
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_page_cas_viewer_set
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}