package com.zaen.testly.cas

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zaen.testly.fragments.base.BaseFragment

class CasViewerPagerAdapter(val context: Context, private val fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager){
    init {
    }

    private val fragmentList: ArrayList<BaseFragment> = arrayListOf()
    private val fragmentTitleList: ArrayList<String> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: BaseFragment, title: String, position: Int){
        fragmentList.add(position, fragment)
        fragmentTitleList.add(position,title)
    }

    fun removeFragment(position: Int){
        fragmentList.removeAt(position)
        fragmentTitleList.removeAt(position)
    }

    fun clearFragments(){
        fragmentList.clear()
        fragmentTitleList.clear()
    }
}