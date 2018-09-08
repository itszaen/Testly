package com.zaen.testly.cas.children.viewer.activities

import android.os.Bundle
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.cas.children.viewer.fragments.pages.CasViewerCardPageFragment
import com.zaen.testly.cas.children.viewer.fragments.pages.CasViewerSetPageFragment
import com.zaen.testly.cas.children.viewer.views.pagers.CasViewerPagerAdapter
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.ARG_DOCUMENT_TYPE
import kotlinx.android.synthetic.main.activity_cas_viewer.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.util.*

class CasViewerActivity: BaseActivity(),
        CreateCasFragment.CasDataListener{
    companion object {
        const val ARG_DOCUMENT_POSITION = "position"
    }
    private var position: Int? = null
    private var pagerAdapter: CasViewerPagerAdapter? = null
    private var hasOnDataCalled = false
    private var hasPositionSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)
        position = intent.extras[ARG_DOCUMENT_POSITION] as Int
        initializeViewPager()
        informActivityLifeCycle("onCreate")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onData(cardList: ArrayList<CardData>, setList: ArrayList<SetData>, isReverseLayout: Boolean) {
        hasOnDataCalled = true
        if (supportFragmentManager == null){ return }
        //if (isReverseLayout){cardList.reverse()}
        pagerAdapter!!.clearFragments()
        when (intent.extras[ARG_DOCUMENT_TYPE]){
            FirebaseDocument.CARD -> {
                lateinit var list: ArrayList<CardData>
                if (isReverseLayout){
                    list = ArrayList(cardList)
                    list.reverse()
                    position = list.size-1 - position!!
                }
                for ((i,card) in list.withIndex()){
                    pagerAdapter!!.addFragment(CasViewerCardPageFragment.newInstance(card),card.title,i)
                }
            }
            FirebaseDocument.SET -> {
                lateinit var list: ArrayList<SetData>
                if (isReverseLayout){
                    list = ArrayList(setList)
                    list.reverse()
                    position = list.size - 1 - position!!
                }
                for ((i,set) in list.withIndex()){
                    pagerAdapter!!.addFragment(CasViewerSetPageFragment.newInstance(set),set.title,i)
                }
            }
        }
        view_pager_cas_viewer.adapter = pagerAdapter
        if (!hasPositionSet){
            if (position != null) {
                view_pager_cas_viewer.currentItem = position!!
                hasPositionSet = true
            }
        }
    }

    private fun initializeViewPager(){
        OverScrollDecoratorHelper.setUpOverScroll(view_pager_cas_viewer)
        pagerAdapter = CasViewerPagerAdapter(this, supportFragmentManager)
        view_pager_cas_viewer.adapter = pagerAdapter
        view_pager_cas_viewer.offscreenPageLimit = 20
        view_pager_cas_viewer.clipChildren = false
        view_pager_cas_viewer.clipToPadding = false
    }

}