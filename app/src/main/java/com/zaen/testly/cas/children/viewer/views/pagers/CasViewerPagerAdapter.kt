package com.zaen.testly.cas.children.viewer.views.pagers

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zaen.testly.base.fragments.BaseFragment

class CasViewerPagerAdapter(val context: Context, private val fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager){
    private val fragmentList: ArrayList<BaseFragment> = arrayListOf()
    private val fragmentTitleList: ArrayList<String> = arrayListOf()

    override fun getItem(position: Int): BaseFragment {
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

    override fun getPageWidth(position: Int): Float {
        return 0.95f
    }
}