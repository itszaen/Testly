package com.zaen.testly.cas.fragments.pages

import android.os.Bundle
import com.zaen.testly.data.CardData
import com.zaen.testly.main.fragments.CreateCasFragment

class CreateCasCardPageFragment : CreateCasPageFragment(),
    CreateCasFragment.CardDataListener{
    companion object {
        fun newInstance(): CreateCasCardPageFragment {
            val fragment = CreateCasCardPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onData(cardList: ArrayList<CardData>, viewMode: Int) {
        updateUI(cardList, viewMode)
    }
}