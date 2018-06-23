package com.zaen.testly.cas.fragments.pages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.*
import android.widget.GridLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.main.fragments.CreateCasFragment
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
import eu.davidea.flexibleadapter.helpers.ActionModeHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.fragment_page_create_cas.*

abstract class CreateCasPageFragment : BaseFragment(),
        android.support.v7.view.ActionMode.Callback,
        FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemLongClickListener,
        CreateCasFragment.ViewModeListener {
    private var mAdapter: FlexibleAdapter<AbstractFlexibleItem<FlexibleViewHolder>>? = null
    private var actionMode: android.support.v7.view.ActionMode? = null
    protected var mActionHelper: ActionModeHelper? = null
    private var viewMode: Int? = null
    internal var dataList: ArrayList<out FirebaseDocument>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_page_create_cas
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            if (mAdapter != null){
                mAdapter!!.onRestoreInstanceState(savedInstanceState)
                mActionHelper?.restoreSelection(activity as AppCompatActivity)
            }
            return
        }
        setUpFAB()
        informFragmentLifeCycle("onActivityCreated")
    }

    private fun setUpFAB(){
//        recycler_fragment_page_create_cas.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0 && fab_menu.visibility == View.VISIBLE){
//                    fab_menu.collapse()
//                }
//            }
//        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mAdapter?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    fun updateUI(){
        if (dataList == null){return}
        when (viewMode) {
            MODE_GRID -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollGridLayoutManager(activity,3,GridLayout.VERTICAL,false)
                if (dataList!!.size > 0) {
                    for (cas in dataList!!) {
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
                initializeAdapter()
            }
            MODE_LIST -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollLinearLayoutManager(activity, LinearLayout.VERTICAL,true)
                mLayoutManager.stackFromEnd = true
                if (dataList!!.size > 0) {
                    for (cas in dataList!!) {
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
                initializeAdapter()
            }
        }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mAdapter?.mode = SelectableAdapter.Mode.IDLE
        actionMode = mode
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        mAdapter?.mode = SelectableAdapter.Mode.IDLE
        actionMode = null
    }

    private fun initializeAdapter(){
        initializeActionModeHelper(SelectableAdapter.Mode.IDLE)
        mAdapter?.addListener(this)
    }

    private fun initializeActionModeHelper(mode: Int){
        mActionHelper = ActionModeHelper(mAdapter!!,R.menu.menu_main,this).withDefaultMode(mode)
    }

    override fun onViewModeChange(viewMode: Int) {
        this.viewMode = viewMode
        updateUI()
    }
}