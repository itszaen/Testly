package com.zaen.testly.intro.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.zaen.testly.R

/**
 * Created by zaen on 3/11/18.
 */
class FaqFragment : Fragment() {
    var activity: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        return inflater.inflate(R.layout.fragment_intro_faq,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }

//    override fun onItemClick(view: View, position: Int) {
//        Toast.makeText(activity?.applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
//    }
}