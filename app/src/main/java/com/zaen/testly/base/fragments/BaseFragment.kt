package com.zaen.testly.base.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.App
import com.zaen.testly.Global
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.utils.LogUtils
import me.yokeyword.fragmentation.SupportFragment

/*
* layoutRes @onCreateView(3)
* retainInstance == true DEFAULT
* NO unbinder
* */

abstract class BaseFragment : SupportFragment(){
    protected var activity: BaseActivity? = null
    var layoutRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.log(this, 2,"onCreate")
        retainInstance = true
        super.onCreate(savedInstanceState)
        App.addFragmentToStack(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.log(this, 2,"onCreateView")
        return if (layoutRes != null) {
            val view = inflater.inflate(layoutRes!!, container, false)
            view
        } else {
            LogUtils.failure(this,5,"No layout resource specified, not inflating.")
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtils.log(this, 2,"onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        LogUtils.log(this, 2,"onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        if (getActivity() is BaseActivity){
            activity = getActivity() as BaseActivity
        }
    }

    override fun onStart() {
        super.onStart()
        LogUtils.log(this, 2,"onStart")
    }

    override fun onDestroyView() {
        LogUtils.log(this, 2,"onDestroyView")
        super.onDestroyView()
        App.removeFragmentFromStack(this)
    }

    protected fun informFragmentLifeCycle(cycle: String){
        val intent = Intent()
        intent.action = Global.KEY_ACTION_INFORM_LIFECYCLE_FRAGMENT
        intent.putExtra(cycle,true)
        intent.putExtras(intent)
//        LocalBroadcastManager.getInstance(this).
        activity?.sendBroadcast(intent)
    }

}