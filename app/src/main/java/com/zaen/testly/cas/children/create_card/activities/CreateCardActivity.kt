package com.zaen.testly.cas.children.create_card.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.cas.children.create_card.fragments.CreateCardFragment
import com.zaen.testly.cas.children.create_card.fragments.PreviewCardFragment
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument.Companion.CARD
import com.zaen.testly.base.fragments.BaseFragment
import org.parceler.Parcels

class CreateCardActivity : BaseActivity(),
    CreateCardFragment.Preview,
    PreviewCardFragment.SubmitCardSuccess{
    var toolbar : ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_card
        super.onCreate(savedInstanceState)

        toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        if (savedInstanceState != null) {
            return
        }
        loadRootFragment(R.id.fragment_container_create_card, CreateCardFragment(),true,true)
    }

    private fun initFragment(newFragment: BaseFragment, title: String){
        start(newFragment)
        toolbar?.title = title
    }

    override fun onPreview(newCard: CardData) {
        val newFragment = PreviewCardFragment()
        val arg = Bundle()
        arg.putParcelable(CARD,Parcels.wrap(newCard))
        newFragment.arguments = arg
        initFragment(newFragment,newCard.title)
    }

    override fun onSubmitSuccessful(){
        finish()
    }

}
