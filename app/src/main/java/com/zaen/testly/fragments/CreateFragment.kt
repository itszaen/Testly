package com.zaen.testly.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.GridLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.iconics.context.IconicsLayoutInflater2
import com.zaen.testly.CreateCasData
import com.zaen.testly.R
import com.zaen.testly.views.recyclers.CreateCasAdapter
import kotlinx.android.synthetic.main.fragment_create.*

class CreateCasFragment : Fragment() {
    var activity: Activity? = null
    private var unbinder: Unbinder? = null

    val MODE_GRID = 1
    val MODE_LIST = 2

    private var viewMode = MODE_GRID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        val view = inflater.inflate(R.layout.fragment_create,container,false)
        unbinder = ButterKnife.bind(this,view)
        setHasOptionsMenu(true)
        return view
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
        activity = getActivity()
        if (savedInstanceState != null){

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
            MODE_GRID -> mlayoutManager = GridLayoutManager(activity,3, GridLayout.VERTICAL,true)
            MODE_LIST -> mlayoutManager = LinearLayoutManager(activity, VERTICAL,true)
        }
        recycler_create.apply {
            layoutManager = mlayoutManager
            adapter = CreateCasAdapter(CreateCasData.dataList)
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
}