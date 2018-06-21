package com.zaen.testly.cas.fragments.pages

import android.os.Bundle

class CreateCasPoolPageFragment : CreateCasPageFragment(){
    companion object {
        fun newInstance(): CreateCasPoolPageFragment {
            val fragment = CreateCasPoolPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

}