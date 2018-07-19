package com.zaen.testly.cas.fragments.pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zaen.testly.cas.children.viewer.activities.CasViewerActivity
import com.zaen.testly.cas.fragments.pages.base.CreateCasPageFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.main.fragments.CreateCasFragment
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.ARG_DOCUMENT_TYPE

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

    override fun onData(cardList: ArrayList<CardData>, isReverseLayout: Boolean) {
        dataList = cardList
        updateUI(isReverseLayout)
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        val intent = Intent(activity!!, CasViewerActivity::class.java)
        intent.putExtra(ARG_DOCUMENT_TYPE, FirebaseDocument.CARD)
        intent.putExtra(CasViewerActivity.ARG_DOCUMENT_POSITION, position)
        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }
}