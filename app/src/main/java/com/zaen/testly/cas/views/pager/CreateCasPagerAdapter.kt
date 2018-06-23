package com.zaen.testly.cas.views.pager

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.cas.fragments.pages.CreateCasCardPageFragment
import com.zaen.testly.cas.fragments.pages.CreateCasPoolPageFragment
import com.zaen.testly.cas.fragments.pages.CreateCasSetPageFragment

class CreateCasPagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    private val fragmentList = arrayListOf(
        CreateCasCardPageFragment.newInstance(),
        CreateCasSetPageFragment.newInstance(),
        CreateCasPoolPageFragment.newInstance()
    )
    private val fragmentTitle = arrayListOf(
            "Cards","Sets","Pools"
    )
    override fun getItem(position: Int): BaseFragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

}
