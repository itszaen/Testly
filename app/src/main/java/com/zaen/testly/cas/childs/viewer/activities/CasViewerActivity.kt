package com.zaen.testly.cas.childs.viewer.activities

import android.content.Intent
import android.os.Bundle
import com.zaen.testly.Global.Companion.KEY_ACTION_INFORM
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.cas.childs.viewer.fragments.pages.CasViewerCardPageFragment
import com.zaen.testly.cas.childs.viewer.fragments.pages.CasViewerSetPageFragment
import com.zaen.testly.cas.childs.viewer.views.pagers.CasViewerPagerAdapter
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment
import kotlinx.android.synthetic.main.activity_cas_viewer.*

class CasViewerActivity: BaseActivity(),
        CreateCasFragment.CasDataListener{
    companion object {
        const val ARG_DOCUMENT_ID = "documentId"
    }
    private var documentId: String? = null
    private var pagerAdapter: CasViewerPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)
        documentId = intent.extras[ARG_DOCUMENT_ID] as String
        initializeViewPager()

        val intent = Intent()
        intent.action = KEY_ACTION_INFORM
        intent.putExtra("onCreate",true)
        intent.putExtras(intent)
//        LocalBroadcastManager.getInstance(this).
        sendBroadcast(intent)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onData(casList: ArrayList<FirebaseDocument>) {
        if (supportFragmentManager == null){ return }

        pagerAdapter!!.clearFragments()
        for ((i,cas) in casList.withIndex()){
            when (cas.type){
                FirebaseDocument.CARD -> pagerAdapter!!.addFragment(CasViewerCardPageFragment.newInstance(cas as CardData),(cas as CardData).title,i)
                FirebaseDocument.SET -> pagerAdapter!!.addFragment(CasViewerSetPageFragment.newInstance(cas as SetData),(cas as SetData).title,i)
            }
        }
        view_pager_cas_viewer.adapter = pagerAdapter
    }

    private fun initializeViewPager(){
        pagerAdapter = CasViewerPagerAdapter(this, supportFragmentManager)
        view_pager_cas_viewer.adapter = pagerAdapter
        view_pager_cas_viewer.offscreenPageLimit = 20
    }

}