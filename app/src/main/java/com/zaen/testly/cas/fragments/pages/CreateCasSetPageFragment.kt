package com.zaen.testly.cas.fragments.pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zaen.testly.cas.childs.viewer.activities.CasViewerActivity
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.ARG_DOCUMENT_TYPE

class CreateCasSetPageFragment : CreateCasPageFragment(),
        CreateCasFragment.SetDataListener{
    companion object {
        fun newInstance(): CreateCasSetPageFragment {
            val fragment = CreateCasSetPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onData(setList: ArrayList<SetData>) {
        dataList = setList
        updateUI()
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        val document = dataList!![position]
        val intent = Intent(activity!!, CasViewerActivity::class.java)
        intent.putExtra(ARG_DOCUMENT_TYPE, FirebaseDocument.SET)
        intent.putExtra(CasViewerActivity.ARG_DOCUMENT_ID, document.id)
        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }
}