package com.zaen.testly.cas.fragments

import android.os.Bundle
import com.zaen.testly.fragments.base.BaseFragment
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder

class CreateCasPageFragment : BaseFragment(){
    companion object {
        fun newInstance(items: AbstractFlexibleItem<FlexibleViewHolder>): CasViewerPageFragment {
            val arg = Bundle()
            arg.put
            val frag = CasViewerPageFragment()
            frag.arguments = arg
            return frag
        }
    }
}