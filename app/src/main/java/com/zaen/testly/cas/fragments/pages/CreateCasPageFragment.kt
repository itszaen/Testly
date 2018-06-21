package com.zaen.testly.cas.fragments.pages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import com.zaen.testly.Global
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.MODE_GRID
import com.zaen.testly.main.fragments.CreateCasFragment.Companion.MODE_LIST
import com.zaen.testly.views.recyclers.items.CasCardGridItem
import com.zaen.testly.views.recyclers.items.CasCardLinearItem
import com.zaen.testly.views.recyclers.items.CasSetGridItem
import com.zaen.testly.views.recyclers.items.CasSetLinearItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.fragment_page_create_cas.*

open class CreateCasPageFragment : BaseFragment() {
    private var mAdapter: FlexibleAdapter<AbstractFlexibleItem<FlexibleViewHolder>>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_page_create_cas
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent()
        intent.action = Global.KEY_ACTION_INFORM
        intent.putExtra("onActivityCreated",true)
        intent.putExtras(intent)
//        LocalBroadcastManager.getInstance(this).
        activity?.sendBroadcast(intent)
    }

    fun updateUI(dataList: ArrayList<out FirebaseDocument>, viewMode: Int){
        when (viewMode) {
            MODE_GRID -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollGridLayoutManager(activity,3,GridLayout.VERTICAL,false)
                if (dataList.size > 0) {
                    for (cas in dataList) {
                        when (cas){
                            is CardData -> items.add(CasCardGridItem(cas))
                            is SetData -> items.add(CasSetGridItem(cas))
                        }
                    }
                }
                mAdapter = FlexibleAdapter(items)
                recycler_fragment_page_create_cas.apply {
                    layoutManager = mLayoutManager
                    adapter = mAdapter
                }
            }
            MODE_LIST -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollLinearLayoutManager(activity, LinearLayout.VERTICAL,false)
                mLayoutManager.stackFromEnd = true
                if (dataList.size > 0) {
                    for (cas in dataList) {
                        when (cas){
                            is CardData -> items.add(CasCardLinearItem(cas))
                            is SetData -> items.add(CasSetLinearItem(cas))
                        }
                    }
                }
                mAdapter = FlexibleAdapter(items)
                recycler_fragment_page_create_cas.apply {
                    layoutManager = mLayoutManager
                    adapter = mAdapter
                }
            }
        }
        mAdapter?.mode = SelectableAdapter.Mode.IDLE
    }

}