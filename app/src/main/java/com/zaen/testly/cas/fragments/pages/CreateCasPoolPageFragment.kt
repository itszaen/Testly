package com.zaen.testly.cas.fragments.pages

import android.os.Bundle
import android.view.View
import com.zaen.testly.cas.fragments.pages.base.CreateCasPageFragment

class CreateCasPoolPageFragment : CreateCasPageFragment(){
    companion object {
        fun newInstance(): CreateCasPoolPageFragment {
            val fragment = CreateCasPoolPageFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
//        val document = mCreateCas.casList[position]
//        val intent = Intent(activity!!, CasViewerActivity::class.java)
//        intent.putExtra(CasViewerActivity.ARG_DOCUMENT_ID,document.id)
//        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
//        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }

}