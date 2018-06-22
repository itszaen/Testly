package com.zaen.testly.main.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import butterknife.OnClick
import butterknife.Optional
import com.zaen.testly.App
import com.zaen.testly.Global.Companion.KEY_ACTION_INFORM_LIFECYCLE_ACTIVITY
import com.zaen.testly.Global.Companion.KEY_ACTION_INFORM_LIFECYCLE_FRAGMENT
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.cas.CreateCasData
import com.zaen.testly.cas.activities.CreateCardActivity
import com.zaen.testly.cas.activities.CreateSetActivity
import com.zaen.testly.cas.views.pager.CreateCasPagerAdapter
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.data.SetData
import kotlinx.android.synthetic.main.fragment_create_cas.*

class CreateCasFragment : BaseFragment(){
    companion object {
        const val MODE_GRID = 1
        const val MODE_LIST = 2
    }

    private var viewMode = MODE_LIST
    private var mCreateCas = CreateCasData(this)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
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

    private fun createReceiver(){
        val receiver = TheBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(KEY_ACTION_INFORM_LIFECYCLE_ACTIVITY)
        intentFilter.addAction(KEY_ACTION_INFORM_LIFECYCLE_FRAGMENT)
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