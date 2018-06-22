package com.zaen.testly.cas.fragments.pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zaen.testly.cas.childs.viewer.activities.CasViewerActivity
import com.zaen.testly.data.CardData
import com.zaen.testly.main.fragments.CreateCasFragment

class CreateCasCardPageFragment : CreateCasPageFragment(),
    CreateCasFragment.CardDataListener{
    companion object {
        fun newInstance(): CreateCasCardPageFragment {
            val fragment = CreateCasCardPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }
    private var cardList: ArrayList<CardData> = arrayListOf()

    override fun onData(cardList: ArrayList<CardData>) {
        this.cardList = cardList
        updateUI(cardList)
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        val document = cardList[position]
        val intent = Intent(activity!!, CasViewerActivity::class.java)
        intent.putExtra(CasViewerActivity.ARG_DOCUMENT_ID,document.id)
        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }
}