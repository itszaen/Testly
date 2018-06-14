package com.zaen.testly.cas.activities

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.cas.CasViewerPagerAdapter
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.fragments.CreateCasFragment

class CasViewerActivity: BaseActivity(),
        CreateCasFragment.CasDataListener{
    companion object {
        const val ARG_DOCUMENT_ID = "documentId"
    }
    private var documentId: String? = null
    private var manager: FragmentManager? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        documentId = intent.extras[ARG_DOCUMENT_ID] as String

        viewPager = findViewById(R.id.view_pager_cas_viewer)

        manager = supportFragmentManager

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onData(casList: ArrayList<FirebaseDocument>) {
        if (manager != null || viewPager == null){ return }
        val pagerAdapter = CasViewerPagerAdapter(manager!!, casList)
        viewPager?.adapter = pagerAdapter
    }

}