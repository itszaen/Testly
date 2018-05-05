package com.zaen.testly.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.zaen.testly.R

/**
 * Created by zaen on 2/27/18.
 */
class PastexamFragment : FileBrowserFragment() {
    companion object {
        const val TAG = "PastexamFragment"
        const val bucket = "testly-2014.appspot.com"
        const val sub = "subdirectories"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_pastexam
        return super.onCreateView(inflater, container, savedInstanceState)
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
