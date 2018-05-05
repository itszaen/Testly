package com.zaen.testly.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout.VERTICAL
import butterknife.OnClick
import butterknife.Optional
import com.zaen.testly.CreateCasData
import com.zaen.testly.R
import com.zaen.testly.activities.CreateCardActivity
import com.zaen.testly.activities.CreateSetActivity
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.views.recyclers.CreateCasGridAdapter
import kotlinx.android.synthetic.main.fragment_create.*

class CreateCasFragment : BaseFragment(),
    CreateCasData.CreateCasDataListener{
    val MODE_GRID = 1
    val MODE_LIST = 2

    private var viewMode = MODE_GRID
    private var mCreateCas = CreateCasData(this)
    private var onGotCasDataFuncCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
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
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recycler
        toggleViewMode()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            return
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

    fun toggleViewMode(){
        var mlayoutManager: RecyclerView.LayoutManager? = null
        when (viewMode) {
            MODE_GRID -> {
                mlayoutManager = GridLayoutManager(activity, 3, VERTICAL, true)
                recycler_create.apply {
                    layoutManager = mlayoutManager
                    adapter = CreateCasGridAdapter(mCreateCas.casList)
                }
            }
            MODE_LIST -> {
                mlayoutManager = LinearLayoutManager(activity, VERTICAL,true)
                recycler_create.apply {
                    layoutManager = mlayoutManager
                    adapter = CreateCasGridAdapter(mCreateCas.casList)
                }
            }
        }
    }

    fun selectMenu(menu: Menu?){
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

    override fun onGotCasData() {
        if (!mCreateCas.isListening){

        }
        if (onGotCasDataFuncCount > 0){
            toggleViewMode()
        }
        onGotCasDataFuncCount += 1
    }

    @Optional
    @OnClick(R.id.fab_create_card,R.id.fab_create_set)
    fun onButtonClick(view: View){
        var intent: Intent? = null
        when(view.id){
            R.id.fab_create_card -> intent = Intent(activity,CreateCardActivity::class.java)
            R.id.fab_create_set  -> intent = Intent(activity, CreateSetActivity::class.java)
        }
        if (intent != null){
            startActivity(intent)
        }
    }
}