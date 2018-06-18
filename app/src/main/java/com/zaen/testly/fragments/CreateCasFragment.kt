package com.zaen.testly.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.*
import android.widget.LinearLayout.VERTICAL
import butterknife.OnClick
import butterknife.Optional
import com.zaen.testly.App
import com.zaen.testly.R
import com.zaen.testly.cas.CreateCasData
import com.zaen.testly.cas.activities.CasViewerActivity
import com.zaen.testly.cas.activities.CasViewerActivity.Companion.ARG_DOCUMENT_ID
import com.zaen.testly.cas.activities.CreateCardActivity
import com.zaen.testly.cas.activities.CreateSetActivity
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.utils.InformUtils
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
import kotlinx.android.synthetic.main.fragment_create.*

class CreateCasFragment : BaseFragment(),
        android.support.v7.view.ActionMode.Callback,
        FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemLongClickListener{
    companion object {
        const val MODE_GRID = 1
        const val MODE_LIST = 2
    }

    private var viewMode = MODE_LIST
    private var mCreateCas = CreateCasData(this)
    private var mAdapter: FlexibleAdapter<AbstractFlexibleItem<FlexibleViewHolder>>? = null
    private var actionMode: android.support.v7.view.ActionMode? = null
    private var mActionHelper: ActionModeHelper? = null
    private var mListener :CasDataListener? = null

    interface CasDataListener{
        fun onData(casList: ArrayList<FirebaseDocument>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_create
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_create,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        selectMenu(menu)
        updateUI()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToCard()
        listenToSet()
    }

    private fun initializeAdapter(){
        mAdapter?.addListener(this)
        initializeActionModeHelper(SelectableAdapter.Mode.IDLE)
    }

    private fun initializeActionModeHelper(mode: Int){
        mActionHelper = ActionModeHelper(mAdapter!!,R.menu.menu_main,this).withDefaultMode(mode)
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
        createReceiver()
    }

    private fun listenToCard(){
        val cardRequest = mCreateCas.createCasRequest(FirebaseDocument.CARD,"timestamp",null)
        mCreateCas.listenToCard(cardRequest!!, object: com.zaen.testly.cas.CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCreateCas.casList.size > 0){
                    updateUI()
                    mListener?.onData(mCreateCas.casList)
                }
            }
        })
    }

    private fun listenToSet(){
        val setRequest = mCreateCas.createCasRequest(FirebaseDocument.SET, "timestamp", null)
        mCreateCas.listenToSet(setRequest!!, object: com.zaen.testly.cas.CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCreateCas.casList.size > 0){
                    updateUI()
                    mListener?.onData(mCreateCas.casList)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (mCreateCas.registrationCard == null){
            listenToCard()
        }
        if (mCreateCas.registrationSet == null){
            listenToSet()
        }
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.put
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_toggle_view_mode_to_grid -> {
                viewMode = MODE_GRID
                activity?.invalidateOptionsMenu()
            }
            R.id.action_toggle_view_mode_to_list -> {
                viewMode = MODE_LIST
                activity?.invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateUI(){
        when (viewMode) {
            MODE_GRID -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollGridLayoutManager(activity,3,VERTICAL,false)
                if (mCreateCas.casList.size > 0) {
                    val casList = mCreateCas.casList
                    for (cas in casList) {
                        when (cas){
                            is CardData -> items.add(CasCardGridItem(cas))
                            is SetData -> items.add(CasSetGridItem(cas))
                        }
                    }
                }
                mAdapter = FlexibleAdapter(items)
                recycler_create.apply {
                    layoutManager = mLayoutManager
                    adapter = mAdapter
                }
            }
            MODE_LIST -> {
                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
                val mLayoutManager = SmoothScrollLinearLayoutManager(activity,VERTICAL,false)
                mLayoutManager.stackFromEnd = true
                if (mCreateCas.casList.size > 0) {
                    val casList = mCreateCas.casList
                    for (cas in casList) {
                        when (cas){
                            is CardData -> items.add(CasCardLinearItem(cas))
                            is SetData -> items.add(CasSetLinearItem(cas))
                        }
                    }
                }
                mAdapter = FlexibleAdapter(items)
                recycler_create.apply {
                    layoutManager = mLayoutManager
                    adapter = mAdapter
                }
            }
        }
        initializeAdapter()
        mAdapter?.mode = SelectableAdapter.Mode.IDLE
    }

    private fun selectMenu(menu: Menu?){
        if (menu != null) {
            when (viewMode) {
                MODE_GRID -> {
                    menu.findItem(R.id.action_toggle_view_mode_to_list).isVisible = true
                    menu.findItem(R.id.action_toggle_view_mode_to_grid).isVisible = false
                }
                MODE_LIST -> {
                    menu.findItem(R.id.action_toggle_view_mode_to_list).isVisible = false
                    menu.findItem(R.id.action_toggle_view_mode_to_grid).isVisible = true
                }
            }
        }

    }

    @Optional
    @OnClick(R.id.fab_create_card,R.id.fab_create_set)
    fun onButtonClick(view: View){
        var intent: Intent? = null
        when(view.id){
            R.id.fab_create_card -> intent = Intent(activity, CreateCardActivity::class.java)
            R.id.fab_create_set  -> intent = Intent(activity, CreateSetActivity::class.java)
        }
        if (intent != null){
            startActivity(intent)
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

    override fun onItemClick(view: View?, position: Int): Boolean {
        val document = mCreateCas.casList[position]
        if (document.type == "set"){
            InformUtils(activity!!).snackyFailure("Set is not implemented yet.")
            return true
        }
        val intent = Intent(activity!!, CasViewerActivity::class.java)
        intent.putExtra(ARG_DOCUMENT_ID,document.id)
        startActivity(intent)
        return true
    }

    override fun onItemLongClick(position: Int) {
        mActionHelper?.onLongClick(activity as AppCompatActivity, position)
    }

    private fun createReceiver(){
        val receiver = TheBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("a")
        activity?.registerReceiver(receiver,intentFilter)
    }

    inner class TheBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            for (activity in App.getActivityStack()){
                if (activity is CasDataListener){
                    mListener = activity
                    if (intent!!.getBooleanExtra("onCreate",false)) {
                        mListener!!.onData(mCreateCas.casList)
                    }
                }
            }
        }

    }

}