package com.zaen.testly.fragments.cas

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import butterknife.OnClick
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.zaen.testly.R
import com.zaen.testly.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.activity_create_card.view.*
import kotlinx.android.synthetic.main.fragment_create_card.*

class CreateCardFragment : BaseFragment() {
    // Card Type
    val CARD_TYPE_UNSORTED = 1
    val CARD_TYPE_SELECTION = 2
    val CARD_TYPE_SELECTION_MULTIPLE = 3
    val CARD_TYPE_SELECTION_MULTIPLE_ORDERED = 4
    val CARD_TYPE_SPELLING = 5
    private var cardType = 1

    // Option
    private var optionNum = 1

    // Whether to have answer card
    var hasAnswerCard = false

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_add_option.setIconResource(IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_add).color(Color.GRAY).sizeDp(10))
    }

    private fun toggleLayout(){
        when (cardType){
            CARD_TYPE_UNSORTED -> {

            }
            CARD_TYPE_SELECTION -> {

            }
            CARD_TYPE_SELECTION_MULTIPLE -> {

            }
            CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {

            }
            CARD_TYPE_SPELLING -> {

            }
        }
        when (hasAnswerCard){
            true ->{}
            false ->{}
        }
    }

    // Radio Buttons
    @OnClick(
            R.id.radio_btn_unsorted,
            R.id.radio_btn_selection,
            R.id.radio_btn_selection_multiple,
            R.id.radio_btn_selection_multiple_ordered,
            R.id.radio_btn_spelling
            )
    fun onCardTypeRadioButtonClicked(radioButton: RadioButton){
        radio_group_create_card_type.clearCheck()
        val isChecked = radioButton.isChecked
        when (radioButton.id){
            R.id.radio_btn_unsorted -> {
                radio_group_create_card_type.check(R.id.radio_btn_unsorted)
                cardType = CARD_TYPE_UNSORTED
            }
            R.id.radio_btn_selection -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection)
                cardType = CARD_TYPE_SELECTION
            }
            R.id.radio_btn_selection_multiple -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection_multiple)
                cardType = CARD_TYPE_SELECTION_MULTIPLE
            }
            R.id.radio_btn_selection_multiple_ordered -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection_multiple_ordered)
                cardType = CARD_TYPE_SELECTION_MULTIPLE_ORDERED
            }
            R.id.radio_btn_spelling -> {
                radio_group_create_card_type.check(R.id.radio_btn_spelling)
                cardType = CARD_TYPE_SPELLING
            }
        }
        toggleLayout()
    }

    @OnClick(R.id.btn_add_option)
    fun onAddOption(view: View){
        // add layout
        optionNum += 1
        toggleLayout()
    }

    @OnClick(R.id.radio_btn_answer_card_false,R.id.radio_btn_answer_card_true)
    fun onAnswerCardRadioButtonClicked(radioButton: RadioButton){
        radio_group_create_card_answer_card.clearCheck()
        val isChecked = radioButton.isChecked
        when (radioButton.id){
            R.id.radio_btn_answer_card_false -> {
                radio_group_create_card_answer_card.check(R.id.radio_btn_answer_card_false)
                hasAnswerCard = false
            }
            R.id.radio_btn_answer_card_true -> {
                radio_group_create_card_answer_card.check(R.id.radio_btn_answer_card_true)
                hasAnswerCard = true
            }
        }
        toggleLayout()
    }

}