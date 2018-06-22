package com.zaen.testly.cas.fragments.pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zaen.testly.cas.childs.viewer.activities.CasViewerActivity
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment

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
    private var setList: ArrayList<SetData> = arrayListOf()

    override fun onData(setList: ArrayList<SetData>, viewMode: Int) {
        setList.clear()
        this.setList = setList
        updateUI(setList, viewMode)
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        val document = setList[position]
        val intent = Intent(activity!!, CasViewerActivity::class.java)
        intent.putExtra(CasViewerActivity.ARG_DOCUMENT_ID,document.id)
        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }
}