package com.zaen.testly.fragments.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import butterknife.ButterKnife
import com.zaen.testly.R

/**
 * Created by zaen on 3/1/18.
 */
class DeveloperSettingsMainFragment : Fragment() {
    var activity: Activity? = null
    lateinit var toolbar : Toolbar
    private var mListener: DeveloperSettingsMainFragment.FragmentClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DeveloperSettingsMainFragment.FragmentClickListener){
            mListener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        return inflater.inflate(R.layout.fragment_settings_developer_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }



    interface FragmentClickListener{
        fun onFragmentCalled(newFragment:Fragment,title:String)
    }

}