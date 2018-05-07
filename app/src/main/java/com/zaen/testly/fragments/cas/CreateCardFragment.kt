package com.zaen.testly.fragments.cas

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.zaen.testly.R
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.TestlyUser
import com.zaen.testly.data.CardData
import com.zaen.testly.data.CardData.Companion.CARD_TYPE_SELECTION
import com.zaen.testly.data.CardData.Companion.CARD_TYPE_SELECTION_MULTIPLE
import com.zaen.testly.data.CardData.Companion.CARD_TYPE_SELECTION_MULTIPLE_ORDERED
import com.zaen.testly.data.CardData.Companion.CARD_TYPE_SPELLING
import com.zaen.testly.data.SelectionCardData
import com.zaen.testly.data.SelectionMultipleCardData
import com.zaen.testly.data.SelectionMultipleOrderedCardData
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.utils.LogUtils.Companion.TAG
import kotlinx.android.synthetic.main.fragment_create_card.*
import mehdi.sakout.fancybuttons.FancyButton

class CreateCardFragment : BaseFragment(){
    companion object {
        val maxOptionNum = 4
        val minOptionNum = 1
    }

    interface Preview{
        fun onPreview(newCard: CardData)
    }
    private var mPreview: Preview? = null

    // Views


    // Subject
    var subject: String? = null

    // Card Type
    private var cardType = CARD_TYPE_SELECTION

    // Option
    private var addingOption = false
    private var removingOption = false
    private var removingIndex: Int? = null
    private var optionNum = 0
    private var optionList: ArrayList<String> = arrayListOf()

    // Answer
    private var answerNum: Int? = null
    private var answerNumMulti: ArrayList<Int> = arrayListOf()

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
        if (context is Preview){
            mPreview = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_create_card
        setHasOptionsMenu(true)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_create_card,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            return
        }
        TestlyUser(this).addUserinfoListener(object: TestlyUser.UserinfoListener {
            override fun onUserinfoUpdate(snapshot: DocumentSnapshot?) {
                if (snapshot != null) {
                    val school = snapshot.get("schoolId") as String
                    val grade = snapshot.get("gradeId") as String
                    TestlyFirestore(this).addDocumentListener(FirebaseFirestore.getInstance().collection("schools").document(school)
                                    .collection("grades").document(grade),object: TestlyFirestore.DocumentListener{
                        override fun handleListener(listener: ListenerRegistration?) {
                        }
                        override fun onDocumentUpdate(path: DocumentReference, snapshot: DocumentSnapshot?, exception: Exception?) {
                            if (snapshot != null) {
                                val subjectList = snapshot.get("subjects") as ArrayList<String>
                                val adapter = ArrayAdapter<String>(activity, R.layout.item_spinner_create_card_subject, subjectList)
                                spinner_subject.setAdapter(adapter)
                                spinner_subject.setOnItemSelectedListener { view, position, id, item ->
                                    subject = item.toString()
                                }
                            }
                        }
                    })
                }
            }
        })

        for (i in 0 until 4) {
            onAddOption(View(activity))
        }
        updateUI()
    }


    private fun updateUI(){
        create_card_options.visibility = View.GONE
        answer_container_create_card_selection.visibility = View.GONE
        answer_container_create_card_selection_multiple.visibility = View.GONE
        answer_container_create_card_selection_multiple_ordered.visibility = View.GONE
        answer_container_create_card_spelling.visibility = View.GONE

        when (cardType){
            CARD_TYPE_SELECTION -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection)
                create_card_options.visibility = View.VISIBLE
                answer_container_create_card_selection.visibility = View.VISIBLE

            }
            CARD_TYPE_SELECTION_MULTIPLE -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection_multiple)
                create_card_options.visibility = View.VISIBLE
                answer_container_create_card_selection_multiple.visibility = View.VISIBLE

            }
            CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                radio_group_create_card_type.check(R.id.radio_btn_selection_multiple_ordered)
                create_card_options.visibility = View.VISIBLE
                answer_container_create_card_selection_multiple_ordered.visibility = View.VISIBLE

            }
            CARD_TYPE_SPELLING -> {
                radio_group_create_card_type.check(R.id.radio_btn_spelling)
                create_card_options.visibility = View.GONE
                answer_container_create_card_spelling.visibility = View.VISIBLE

            }
        }

        // When Adding Option
        if (addingOption){
            optionNum += 1
            // Option Num
            /// Done in RecyclerView

            // Answer Num (Selection)
            val radioBtn = RadioButton(activity)
            val param2 = RadioGroup.LayoutParams(0,RadioGroup.LayoutParams.WRAP_CONTENT,1F)
            radioBtn.layoutParams = param2
            radioBtn.text = optionNum.toString()
            radioBtn.setOnClickListener{
                onAnswerNumRadioButtonClicked(it)
            }
            radio_group_create_card_selection_answer.addView(radioBtn)

            // Answer Num (Multiple)
            val checkBox = CheckBox(activity)
            val param3 = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1F)
            checkBox.layoutParams = param3
            checkBox.text = optionNum.toString()
            checkBox.setOnCheckedChangeListener{view,isChecked ->
                onAnswerNumCheckBoxChanged(view,isChecked)
            }
            answer_container_create_card_selection_multiple.addView(checkBox)

            addingOption = false
        }
        // When Removing Option
        if (removingOption){
            if (removingIndex != null) {
                optionNum -= 1
                // Option
                /// Done in RecyclerView
                // Answer Num (Selection)
                radio_group_create_card_selection_answer.removeViewAt(removingIndex!!)
                if (removingIndex == answerNum){
                    answerNum = null
                }
                // Answer Num (Multiple)
                answer_container_create_card_selection_multiple.removeViewAt(removingIndex!!)
                for ((i,v) in answerNumMulti.withIndex()){
                    if (v == removingIndex) {
                        answerNumMulti.remove(i)
                        break
                    }
                }

                removingOption = false
                removingIndex = null
            }
        }

        // Recycler View
        input_container_create_card_options.apply{
            layoutManager = GridLayoutManager(activity,2,GridLayout.VERTICAL,false)
            adapter = OptionAdapter(optionList,optionNum,this@CreateCardFragment)
        }

        // Option Add Button
        if (optionNum >= maxOptionNum){
            btn_add_option.setOnClickListener(null)
            btn_add_option.isEnabled = false
        } else {
            btn_add_option.isEnabled = true
            ButterKnife.bind(this, btn_add_option)
        }
        // Option Counter
        count_create_card_options_num.text = optionNum.toString()

        // Option number
//        val optionLayoutNum = input_container_create_card_options.childCount
//        for (i in 0 until optionLayoutNum){
//            val optionLayout = input_container_create_card_options.getChildAt(i) as LinearLayout
//            val optionLayoutNum = optionLayout.getChildAt(1) as TextView
//            optionLayoutNum.text = (i+1).toString() + "."
//        }

        // Answer
        if (optionNum > 0) {
            /// Selection
            if (answerNum == null) {
                answerNum = 0
                (radio_group_create_card_selection_answer.getChildAt(answerNum!!) as RadioButton).isChecked = true
            } else {
                (radio_group_create_card_selection_answer.getChildAt(answerNum!!) as RadioButton).isChecked = true

            }
            /// Multiple
            if (answerNumMulti.isEmpty()) {
                answerNumMulti.add(0)
                (answer_container_create_card_selection_multiple.getChildAt(0) as CheckBox).isChecked = true
            }
            /// Keep the order
            for (i in 0 until radio_group_create_card_selection_answer.childCount) {
                (radio_group_create_card_selection_answer.getChildAt(i) as RadioButton).text = (i + 1).toString()
            }
            for (i in 0 until answer_container_create_card_selection_multiple.childCount) {
                (answer_container_create_card_selection_multiple.getChildAt(i) as CheckBox).text = (i + 1).toString()
            }
        }

        // Answer Card
        when (hasAnswerCard){
            true ->{
                radio_group_create_card_answer_card.check(R.id.radio_btn_answer_card_true)
                input_create_card_answer_card.visibility = View.VISIBLE
            }
            false ->{
                radio_group_create_card_answer_card.check(R.id.radio_btn_answer_card_false)
                input_create_card_answer_card.visibility = View.GONE
            }
        }

        // Debug
        for ((i,s) in optionList.withIndex()) {
            Log.d(TAG(this), "Array content no.${i+1}: $s")
        }
    }

    // Card Type Radio Buttons
    @Optional
    @OnClick(
            R.id.radio_btn_selection,
            R.id.radio_btn_selection_multiple,
            R.id.radio_btn_selection_multiple_ordered,
            R.id.radio_btn_spelling
            )
    fun onCardTypeRadioButtonClicked(radioButton: RadioButton){
        radio_group_create_card_type.clearCheck()
        val isChecked = radioButton.isChecked
        when (radioButton.id){
            R.id.radio_btn_selection -> {
                cardType = CARD_TYPE_SELECTION
            }
            R.id.radio_btn_selection_multiple -> {
                cardType = CARD_TYPE_SELECTION_MULTIPLE
            }
            R.id.radio_btn_selection_multiple_ordered -> {
                cardType = CARD_TYPE_SELECTION_MULTIPLE_ORDERED
            }
            R.id.radio_btn_spelling -> {
                cardType = CARD_TYPE_SPELLING
            }
        }
        updateUI()
    }

    // Option Buttons
    /// Add option
    @Optional
    @OnClick(R.id.btn_add_option)
    fun onAddOption(view: View){
        // add layout
        addingOption = true
        updateUI()
    }

    /// Remove Option
//    @Optional
//    @OnClick(R.id.btn_remove_option)
    fun onRemoveOption(index: Int){
        removingOption = true
        removingIndex = index
        updateUI()
    }

    // Answer Number (Selection)
    fun onAnswerNumRadioButtonClicked(view: View){
        val index = (view.parent as RadioGroup).indexOfChild(view)
        answerNum = index
        updateUI()
    }

    // Answer Number (Multiple)
    fun onAnswerNumCheckBoxChanged(view: View, isChecked: Boolean){
        val index = (view.parent as LinearLayout).indexOfChild(view)
        if (isChecked) {
            if (!answerNumMulti.contains(index)) {
                answerNumMulti.add(index)
            }
        } else {
            answerNumMulti.remove(index)
        }
        updateUI()
    }

    // Answer Card Radio Buttons
    @Optional
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
        updateUI()
    }

    fun checkNoError():Boolean{
        var cancel = false
        var focusView : View? = null

        input_create_card_title.error = null
        input_create_card_question.error = null
        error_create_card_option_cannot_be_blank.visibility = View.GONE
        error_create_card_answer_not_enough.visibility = View.GONE
        error_create_card_mask_not_enough.visibility = View.GONE
        input_create_card_answer_card.error = null

        // For the first view to receive focusView, hence the order.

        if (hasAnswerCard){
            if (TextUtils.isEmpty(edit_create_card_answer_card.text)){
                input_create_card_answer_card.error = activity!!.resources.getString(R.string.error_field_required)
                focusView = edit_create_card_answer_card
                cancel = true
            }
        }

        when (cardType){
            CARD_TYPE_SELECTION->{
                if (answerNum == null){
                    error_create_card_answer_not_enough.visibility = View.VISIBLE
                    focusView = radio_group_create_card_selection_answer
                    cancel = true
                }
                if (optionList.contains("")){
                    error_create_card_option_cannot_be_blank.visibility = View.VISIBLE
                    focusView = answer_container_create_card_selection_multiple
                    cancel = true
                }
            }
            CARD_TYPE_SELECTION_MULTIPLE -> {
                if (answerNumMulti.size < 2){
                    error_create_card_answer_not_enough.visibility = View.VISIBLE
                    focusView = answer_container_create_card_selection_multiple
                    cancel = true
                }
            }
            CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                if (answerNumMulti.size < 2){
                    error_create_card_answer_not_enough.visibility = View.VISIBLE
                    focusView = answer_container_create_card_selection_multiple_ordered
                    cancel = true
                }
            }
            CARD_TYPE_SPELLING -> {
                if (true){
                    error_create_card_mask_not_enough.visibility = View.VISIBLE
                    focusView = answer_container_create_card_spelling
                    cancel = true
                }
            }
        }

        // Check if the question is blank
        if (TextUtils.isEmpty(edit_create_card_question.text)){
            input_create_card_question.error = activity!!.resources.getString(R.string.error_field_required)
            focusView = edit_create_card_question
            cancel = true
        }

        // Check if the title is blank
        if (TextUtils.isEmpty(edit_create_card_title.text)){
            input_create_card_title.error = activity!!.resources.getString(R.string.error_field_required)
            focusView = edit_create_card_title
            cancel = true
        }

        return if (cancel){
            focusView?.requestFocus()
            false
        } else {
            true
        }
    }

    private class OptionAdapter(var mOptionList: ArrayList<String>, val optionNum: Int, val context: CreateCardFragment) : RecyclerView.Adapter<OptionAdapter.OptionHolder>(){
        private var mRecyclerView: RecyclerView? = null

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            mRecyclerView = recyclerView
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            mRecyclerView = null

        }
        override fun getItemCount():Int{
            return optionNum
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder{
            return OptionHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_linear_btn_option,parent,false), context, mOptionList)
        }

        override fun onBindViewHolder(holder:OptionHolder, position: Int) {
            holder.bind(position,optionNum)
        }

        class OptionHolder(view: View, val context: CreateCardFragment, var mOptionList: ArrayList<String>) : RecyclerView.ViewHolder(view){
            private val btn: FancyButton = view.findViewById(R.id.btn_remove_option)
            private val textOptionNum: TextView = view.findViewById(R.id.text_create_card_option_number)
            private val inputOption: android.support.design.widget.TextInputLayout = view.findViewById(R.id.input_create_card_option)
            private val editOption: android.support.design.widget.TextInputEditText = view.findViewById(R.id.edit_create_card_option)
            fun bind(position: Int,optionNum: Int){
                if (optionNum != 0) {
                    if (mOptionList.size < position+1){
                        mOptionList.add("")
                    }
                    if (optionNum <= minOptionNum) {
                        btn.setOnClickListener(null)
                        btn.isEnabled = false
                    } else {
                        btn.isEnabled = true
                        btn.setOnClickListener {
                            removeAt(position)
                        }
                    }
                }
                textOptionNum.text = (position+1).toString() + "."
                editOption.setText(mOptionList[position],TextView.BufferType.EDITABLE)
                editOption.addTextChangedListener(object: TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                    override fun afterTextChanged(s: Editable?) {
                        mOptionList[position] = s.toString()
                    }
                })
            }

            fun removeAt(position: Int){
                mOptionList.removeAt(position)
                context.onRemoveOption(position)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_preview -> {
                preview()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun preview(){
        if (!checkNoError()){
            return
        }

        var newCard: CardData? = null
        val id = ""
        val timestamp = 0L
        val title = edit_create_card_title.text.toString()
        val subject = this.subject.toString()
        val hasAnswerCard = this.hasAnswerCard
        val question = edit_create_card_question.text.toString()
        var answerText: String? = null
        if (hasAnswerCard){
            answerText = edit_create_card_answer_card.text.toString()
        }

        when (cardType){
            CARD_TYPE_SELECTION -> {
                val optionList = this.optionList
                val answer = this.answerNum!!
                newCard = SelectionCardData(id,timestamp,title,subject,hasAnswerCard,question,optionList,answer,answerText)
            }
            CARD_TYPE_SELECTION_MULTIPLE -> {
                val optionList = this.optionList
                val answers = this.answerNumMulti
                newCard = SelectionMultipleCardData(id,timestamp,title,subject,hasAnswerCard,question,optionList,answers,answerText)
            }
            CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                val optionList = this.optionList
                val answers = this.answerNumMulti
                newCard = SelectionMultipleOrderedCardData(id,timestamp,title,subject,hasAnswerCard,question,optionList,answers,answerText)
            }
            CARD_TYPE_SPELLING ->{

            }
        }

        if (newCard != null){
            mPreview?.onPreview(newCard)
        }
    }
}