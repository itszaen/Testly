package com.zaen.testly.cas.fragments.pages

import android.os.Bundle
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment

class CreateCasSetPageFragment : CreateCasPageFragment(),
        CreateCasFragment.SetDataListener{
    companion object {
        fun newInstance(): CreateCasSetPageFragment {
            val fragment = CreateCasSetPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onData(setList: ArrayList<SetData>, viewMode: Int) {
        updateUI(setList, viewMode)
    }
}