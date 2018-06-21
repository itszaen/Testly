package com.zaen.testly.main.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.*
import butterknife.OnClick
import butterknife.Optional
import com.zaen.testly.App
import com.zaen.testly.Global.Companion.KEY_ACTION_INFORM
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.cas.CreateCasData
import com.zaen.testly.cas.activities.CreateCardActivity
import com.zaen.testly.cas.activities.CreateSetActivity
import com.zaen.testly.cas.childs.viewer.activities.CasViewerActivity
import com.zaen.testly.cas.childs.viewer.activities.CasViewerActivity.Companion.ARG_DOCUMENT_ID
import com.zaen.testly.cas.views.pager.CreateCasPagerAdapter
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.helpers.ActionModeHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.fragment_create_cas.*

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
    private var mCasDataListener : CasDataListener? = null
    private var mCardDataListener : CardDataListener? = null
    private var mSetDataListener : SetDataListener? = null
    private var pagerAdapter: CreateCasPagerAdapter? = null

    interface CasDataListener{
        fun onData(casList: ArrayList<FirebaseDocument>)
    }

    interface CardDataListener{
        fun onData(cardList: ArrayList<CardData>, viewMode: Int)
    }

    interface SetDataListener{
        fun onData(setList: ArrayList<SetData>, viewMode: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_create_cas
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_create,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        selectMenu(menu)
        //updateUI()
        super.onPrepareOptionsMenu(menu)
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
        listenToCard()
        listenToSet()
        createReceiver()
        initializeViewPager()
    }
    private fun initializeViewPager(){
        pagerAdapter = CreateCasPagerAdapter(activity!!, childFragmentManager)
        view_pager_fragment_create_cas.adapter = pagerAdapter
        tab_view_pager_create_cas.setupWithViewPager(view_pager_fragment_create_cas)
    }

    private fun listenToCard(){
        val cardRequest = mCreateCas.createCasRequest(FirebaseDocument.CARD,"timestamp",null)
        mCreateCas.listenToCard(cardRequest!!, object: com.zaen.testly.cas.CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCreateCas.casList.size > 0){ onData() }
            }
        })
    }

    private fun listenToSet(){
        val setRequest = mCreateCas.createCasRequest(FirebaseDocument.SET, "timestamp", null)
        mCreateCas.listenToSet(setRequest!!, object: com.zaen.testly.cas.CreateCasData.CreateCasDataListener{
            override fun onCasData() {
                if (mCreateCas.casList.size > 0){ onData() }
            }
        })
    }

    fun onData(){
        //updateUI()
        mCasDataListener?.onData(mCreateCas.casList)
        mCardDataListener?.onData(mCreateCas.cardList, viewMode)
        mSetDataListener?.onData(mCreateCas.setList, viewMode)
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

//    fun updateUI(){
//        when (viewMode) {
//            MODE_GRID -> {
//                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
//                val mLayoutManager = SmoothScrollGridLayoutManager(activity,3,VERTICAL,false)
//                if (mCreateCas.casList.size > 0) {
//                    val casList = mCreateCas.casList
//                    for (cas in casList) {
//                        when (cas){
//                            is CardData -> items.add(CasCardGridItem(cas))
//                            is SetData -> items.add(CasSetGridItem(cas))
//                        }
//                    }
//                }
//                mAdapter = FlexibleAdapter(items)
//                recycler_create_cas.apply {
//                    layoutManager = mLayoutManager
//                    adapter = mAdapter
//                }
//            }
//            MODE_LIST -> {
//                val items: MutableList<AbstractFlexibleItem<FlexibleViewHolder>> = mutableListOf()
//                val mLayoutManager = SmoothScrollLinearLayoutManager(activity,VERTICAL,false)
//                mLayoutManager.stackFromEnd = true
//                if (mCreateCas.casList.size > 0) {
//                    val casList = mCreateCas.casList
//                    for (cas in casList) {
//                        when (cas){
//                            is CardData -> items.add(CasCardLinearItem(cas))
//                            is SetData -> items.add(CasSetLinearItem(cas))
//                        }
//                    }
//                }
//                mAdapter = FlexibleAdapter(items)
//                recycler_create_cas.apply {
//                    layoutManager = mLayoutManager
//                    adapter = mAdapter
//                }
//            }
//        }
//        initializeAdapter()
//        mAdapter?.mode = SelectableAdapter.Mode.IDLE
//    }

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
        intentFilter.addAction(KEY_ACTION_INFORM)
        activity?.registerReceiver(receiver,intentFilter)
    }

    inner class TheBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            for (activity in App.getActivityStack()){
                if (activity is CasDataListener){
                    mCasDataListener = activity
                    if (intent!!.getBooleanExtra("onCreate",false)) {
                        mCasDataListener!!.onData(mCreateCas.casList)
                    }
                }
            }
            for (fragment in App.getFragmentStack()){
                if (fragment is CardDataListener){
                    mCardDataListener = fragment
                    if (intent!!.getBooleanExtra("onActivityCreated",false)){
                        mCardDataListener!!.onData(mCreateCas.cardList, viewMode)
                    }
                }
                if (fragment is SetDataListener){
                    mSetDataListener = fragment
                    if (intent!!.getBooleanExtra("onActivityCreated",false)){
                        mSetDataListener!!.onData(mCreateCas.setList, viewMode)
                    }
                }
            }
        }
    }
}