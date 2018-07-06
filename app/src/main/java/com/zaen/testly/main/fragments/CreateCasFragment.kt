package com.zaen.testly.main.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import com.getbase.floatingactionbutton.FloatingActionsMenu
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
        const val ARG_DOCUMENT_TYPE = "type"
    }

    private var viewMode = MODE_LIST
    private var mCreateCas = CreateCasData(this)
    private var mCasDataListener : CasDataListener? = null
    private var mCardDataListener : CardDataListener? = null
    private var mSetDataListener : SetDataListener? = null
    private var mViewModeListener: ArrayList<ViewModeListener> = arrayListOf()
    private var pagerAdapter: CreateCasPagerAdapter? = null

    interface CasDataListener{
        fun onData(cardList: ArrayList<CardData>, setList: ArrayList<SetData>)
    }

    interface CardDataListener{
        fun onData(cardList: ArrayList<CardData>)
    }

    interface SetDataListener{
        fun onData(setList: ArrayList<SetData>)
    }

    interface ViewModeListener{
        fun onViewModeChange(viewMode: Int)
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
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOverlay()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            return
        }
        createReceiver()
        initializeViewPager()
        listenToCard()
        listenToSet()
    }

    override fun onBackPressedSupport(): Boolean {
        return if (fab_menu.isExpanded){
            fab_menu.collapse()
            true
        } else {
            super.onBackPressedSupport()
        }
    }
    

    private fun initializeViewPager(){
        pagerAdapter = CreateCasPagerAdapter(activity!!, childFragmentManager)
        view_pager_fragment_create_cas.adapter = pagerAdapter
        view_pager_fragment_create_cas.offscreenPageLimit = pagerAdapter!!.getFragmentSize() - 1
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
        mCasDataListener?.onData(mCreateCas.cardList, mCreateCas.setList)
        mCardDataListener?.onData(mCreateCas.cardList)
        mSetDataListener?.onData(mCreateCas.setList)
    }

    override fun onStop() {
        super.onStop()
//        mCreateCas.stopListenToCard()
//        mCreateCas.stopListenToSet()
    }

    override fun onResume() {
        super.onResume()
//        if (mCreateCas.registrationCard == null){
//            listenToCard()
//        }
//        if (mCreateCas.registrationSet == null){
//            listenToSet()
//        }
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
            notifyViewModeChange()
        }
    }

    private fun notifyViewModeChange(){
        for (fragment in mViewModeListener){
            fragment.onViewModeChange(viewMode)
        }
    }

    private fun initializeViews(){
        view_overlay.setOnClickListener{
            fadeOutOverlay()
            fab_menu.collapse()
        }

        fab_create_card.setOnClickListener {
            val intent = Intent(activity, CreateCardActivity::class.java)
            startActivity(intent)
        }
        fab_create_set.setOnClickListener {
            val intent = Intent(activity, CreateSetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpOverlay(){
        fab_menu.setOnFloatingActionsMenuUpdateListener(object: FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{
            override fun onMenuExpanded() {
                fadeInOverlay()
                view_overlay.isClickable = true
                view_overlay.isFocusable = true
            }

            override fun onMenuCollapsed() {
                fadeOutOverlay()
                view_overlay.isClickable = false
                view_overlay.isFocusable = false
            }
        })
    }

    private fun fadeInOverlay(){
        val fadeIn = AlphaAnimation(0F,1F)
        fadeIn.duration = 300
        view_overlay.visibility = View.VISIBLE
        view_overlay.animation = fadeIn
    }

    private fun fadeOutOverlay(){
        val fadeOut = AlphaAnimation(1F,0F)
        fadeOut.duration = 300
        view_overlay.visibility = View.INVISIBLE
        view_overlay.animation = fadeOut
    }

    private fun createReceiver(){
        val receiver = TheBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(KEY_ACTION_INFORM_LIFECYCLE_ACTIVITY)
        intentFilter.addAction(KEY_ACTION_INFORM_LIFECYCLE_FRAGMENT)
        activity?.registerReceiver(receiver, intentFilter)
    }

    inner class TheBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            for (activity in App.getActivityStack()){
                if (activity is CasDataListener){
                    mCasDataListener = activity
                    if (intent!!.getBooleanExtra("onCreate",false)) {
                        mCasDataListener?.onData(mCreateCas.cardList,mCreateCas.setList)
                    }
                }
            }
            for (fragment in App.getFragmentStack()){
                if (fragment is CardDataListener){
                    mCardDataListener = fragment
                    if (intent!!.getBooleanExtra("onActivityCreated",false)){
                        mCardDataListener!!.onData(mCreateCas.cardList)
                    }
                }
                if (fragment is SetDataListener){
                    mSetDataListener = fragment
                    if (intent!!.getBooleanExtra("onActivityCreated",false)){
                        mSetDataListener!!.onData(mCreateCas.setList)
                    }
                }
                if (fragment is ViewModeListener){
                    if (!mViewModeListener.contains(fragment)){
                        mViewModeListener.add(fragment)
                    }
                    notifyViewModeChange()
                }
            }
        }
    }
}