package com.zaen.testly.fragments.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.zaen.testly.utils.LogUtils.Companion.TAG
import me.yokeyword.fragmentation.SupportFragment


abstract class BaseFragment : SupportFragment(){
    companion object {
    }

    protected var unbinder: Unbinder? = null
    protected var activity: Activity? = null
    var layoutRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG(this),"onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG(this),"onCreateView")
        if (layoutRes != null) {
            val view = inflater.inflate(layoutRes!!, container, false)
            unbinder = ButterKnife.bind(this, view)
            return view
        } else {
            Log.w(TAG(this),"No layout resource specified, not inflating.")
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG(this),"onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG(this),"onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG(this),"onStart")
    }

    override fun onDestroyView() {
        Log.d(TAG(this),"onDestroyView")
        super.onDestroyView()
        unbinder?.unbind()
    }

}