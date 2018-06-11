package com.zaen.testly.cas

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaen.testly.CreateCasData
import com.zaen.testly.cas.fragments.CasViewerPageFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData

class CasViewerPagerAdapter(fragmentManager: FragmentManager, private val mCas: CreateCasData): FragmentPagerAdapter(fragmentManager){
    private var max: Int? = null
    init {
        max = mCas.casList.size
    }
    override fun getItem(position: Int): Fragment {
        // tell fragment what document (card/set) it is
        return when (mCas.casList[position]){
            is CardData -> CasViewerPageFragment.newInstance(FirebaseDocument.CARD)
            is SetData  -> CasViewerPageFragment.newInstance(FirebaseDocument.SET)
            else -> CasViewerPageFragment.newInstance("error")
        }
    }

    override fun getCount(): Int {
        return max!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val cas = mCas.casList[position]
        return when (cas.type){
            FirebaseDocument.CARD -> (cas as CardData).title
            FirebaseDocument.SET  -> (cas as SetData).title
            else -> null
        }
    }
}