package com.badmitry.translater.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.badmitry.translater.R
import com.badmitry.translater.databinding.SearchDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchDialogFragment : BottomSheetDialogFragment() {

    private var onSearchClickListener: OnSearchClickListener? = null
    private var binding: SearchDialogFragmentBinding? = null

    private val textWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding?.let{
                if (it.searchEditText.text != null && !it.searchEditText.text.toString().isEmpty()) {
                    it.searchButtonTextview.isEnabled = true
                    it.clearTextImageview.visibility = View.VISIBLE
                } else {
                    it.searchButtonTextview.isEnabled = false
                    it.clearTextImageview.visibility = View.GONE
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}
    }

    private val onSearchButtonClickListener =
        View.OnClickListener {
            binding?.let{
                onSearchClickListener?.onClick(it.searchEditText.text.toString())
            }
            dismiss()
        }

    internal fun setOnSearchClickListener(listener: OnSearchClickListener) {
        onSearchClickListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.search_dialog_fragment, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let{
            it.searchButtonTextview.setOnClickListener(onSearchButtonClickListener)
            it.searchEditText.addTextChangedListener(textWatcher)
        }
        addOnClearClickListener()
    }

    override fun onDestroyView() {
        onSearchClickListener = null
        super.onDestroyView()
    }

    private fun addOnClearClickListener() {
        binding?.let {bind ->
            bind.clearTextImageview.setOnClickListener {
                bind.searchEditText.setText("")
                bind.searchButtonTextview.isEnabled = false
            }
        }
    }

    interface OnSearchClickListener {
        fun onClick(searchWord: String)
    }

    companion object {
        fun newInstance(): SearchDialogFragment {
            return SearchDialogFragment()
        }
    }
}
