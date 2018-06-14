package com.zaen.testly.cas

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaen.testly.cas.fragments.CasViewerPageCardFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.fragments.base.BlankFragment

class CasViewerPagerAdapter(fragmentManager: FragmentManager, private val cas: ArrayList<FirebaseDocument>): FragmentPagerAdapter(fragmentManager){
    init {
    }
    override fun getItem(position: Int): Fragment {
        // tell fragment what document (card/set) it is
        return when (cas[position]){
            is CardData -> CasViewerPageCardFragment.newInstance()
            //is SetData  -> CasViewerPageCardFragment.newInstance(FirebaseDocument.SET)
            else -> BlankFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return cas.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val cas = cas[position]
        return when (cas.type){
            FirebaseDocument.CARD -> (cas as CardData).title
            FirebaseDocument.SET  -> (cas as SetData).title
            else -> null
        }
    }
}