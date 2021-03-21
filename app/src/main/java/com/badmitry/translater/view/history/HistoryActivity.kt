package com.badmitry.translater.view.history

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.badmitry.data.AppState
import com.badmitry.data.DataModel
import com.badmitry.translater.R
import com.badmitry.translater.databinding.HistoryLayoutBinding
import com.badmitry.translater.view.base.BaseActivity
import com.badmitry.translater.view.history.adapter.HistoryAdapter
import com.badmitry.translater.view.main.HistoryInteractor
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>() {

    override lateinit var model: HistoryViewModel
    private var binding: HistoryLayoutBinding? = null
    private val adapter: HistoryAdapter by lazy { HistoryAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.history_layout)
        iniViewModel()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        binding?.historyActivityRecyclerview?.let{
            if (it.adapter != null) {
                throw IllegalStateException("The ViewModel should be initialised first")
            }
            val viewModel: HistoryViewModel by currentScope.inject()
            model = viewModel
            model.subscribe().observe(this@HistoryActivity, Observer<AppState> { renderData(it) })
        }
    }

    private fun initViews() {
        binding?.historyActivityRecyclerview?.adapter = adapter
    }

    override fun showViewSuccess() {
        println("success")
    }

    override fun showViewLoading() {
        println("loading")
    }
}
