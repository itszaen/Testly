package com.zaen.testly.fragments.cas

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.zaen.testly.R
import com.zaen.testly.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.activity_create_card.view.*
import kotlinx.android.synthetic.main.fragment_create_card.*

class CreateCardFragment : BaseFragment() {
    var mListener :FragmentClickListener? = null
    interface FragmentClickListener{
        fun onFragmentCalled(newFragment: Fragment, title:String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentClickListener){
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_create_card
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_option.setIconResource(IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_add).color(Color.GRAY).sizeDp(15))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @OnClick(R.id.btn_add_option)
    fun onAddOption(view: View){

    }

}