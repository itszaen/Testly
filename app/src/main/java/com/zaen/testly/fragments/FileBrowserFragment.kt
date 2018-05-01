package com.zaen.testly.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import butterknife.ButterKnife
import com.zaen.testly.activities.ImageViewActivity
import com.zaen.testly.fragments.base.BaseFragment

/**
 * Created by zaen on 4/4/18.
 */
open class FileBrowserFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun viewImage(path: String){
        val intent = Intent(activity, ImageViewActivity::class.java)
        intent.putExtra("strPath",path)
        startActivity(intent)
    }

    interface RenameToolbar {
        fun renameToolbar(new: String)
    }

}