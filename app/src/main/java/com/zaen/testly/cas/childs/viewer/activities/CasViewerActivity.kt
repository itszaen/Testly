package com.zaen.testly.cas.childs.viewer.activities

import android.os.Bundle
import com.zaen.testly.R
import com.zaen.testly.R.id.view_pager_cas_viewer
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.cas.childs.viewer.fragments.pages.CasViewerCardPageFragment
import com.zaen.testly.cas.childs.viewer.fragments.pages.CasViewerSetPageFragment
import com.zaen.testly.cas.childs.viewer.views.pagers.CasViewerPagerAdapter
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.ARG_DOCUMENT_TYPE
import kotlinx.android.synthetic.main.activity_cas_viewer.*

class CasViewerActivity: BaseActivity(),
        CreateCasFragment.CasDataListener{
    companion object {
        const val ARG_DOCUMENT_ID = "documentId"
    }
    private var documentId: String? = null
    private var pagerAdapter: CasViewerPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO [A2] Dark Background
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)
        documentId = intent.extras[ARG_DOCUMENT_ID] as String
        initializeViewPager()
        informActivityLifeCycle("onCreate")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onData(cardList: ArrayList<CardData>, setList: ArrayList<SetData>) {
        if (supportFragmentManager == null){ return }
        pagerAdapter!!.clearFragments()
        when (intent.extras[ARG_DOCUMENT_TYPE]){
            FirebaseDocument.CARD -> {
                for ((i,card) in cardList.withIndex()){
                    pagerAdapter!!.addFragment(CasViewerCardPageFragment.newInstance(card),card.title,i)
                }
            }
            FirebaseDocument.SET -> {
                for ((i,set) in setList.withIndex()){
                    pagerAdapter!!.addFragment(CasViewerSetPageFragment.newInstance(set),set.title,i)
                }
            }
        }
        view_pager_cas_viewer.adapter = pagerAdapter
    }

    private fun initializeViewPager(){
        pagerAdapter = CasViewerPagerAdapter(this, supportFragmentManager)
        view_pager_cas_viewer.adapter = pagerAdapter
        view_pager_cas_viewer.offscreenPageLimit = 20
        view_pager_cas_viewer.clipChildren = false
        view_pager_cas_viewer.clipToPadding = false
    }

}