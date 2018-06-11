package com.zaen.testly.cas.activities

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.zaen.testly.CreateCasData
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.cas.CasViewerPagerAdapter
import com.zaen.testly.data.FirebaseDocument
import kotlinx.android.synthetic.main.activity_cas_viewer.*

class CasViewerActivity: BaseActivity(){
    companion object {
        const val ARG_DOCUMENT_ID = "documentId"
    }
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        val mCas = CreateCasData(this)

        val cardRequest = mCas.createCasRequest(FirebaseDocument.CARD,"timestamp",null)
        mCas.listenToCard(cardRequest!!, object: CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCas.casList.size > 0){
                    toggleViewMode()
                }
            }
        })
        val setRequest = mCreateCas.createCasRequest(FirebaseDocument.SET, "timestamp", null)
        mCreateCas.listenToSet(setRequest!!, object: CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCreateCas.casList.size > 0){
                    toggleViewMode()
                }
            }
        })

        documentId = intent.extras[ARG_DOCUMENT_ID] as String

        val viewPager = findViewById<ViewPager>(R.id.view_pager_cas_viewer)

        val manager = supportFragmentManager
        val pagerAdapter = CasViewerPagerAdapter(manager, mCas)
        viewPager.adapter = pagerAdapter


    }

    override fun onStart() {
        super.onStart()
    }


}