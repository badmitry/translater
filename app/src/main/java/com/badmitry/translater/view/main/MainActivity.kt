package com.badmitry.translater.view.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.badmitry.translater.R
import com.badmitry.translater.databinding.MainLayoutBinding
import com.badmitry.translater.presenter.IPresenter
import com.badmitry.translater.view.base.BaseActivity
import com.badmitry.translater.view.base.IView
import com.badmitry.translater.view.main.adapter.MainAdapter
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel

class MainActivity : BaseActivity<AppState>() {

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }


    private var binding: MainLayoutBinding? = null
    private var adapter: MainAdapter? = null
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    override fun createPresenter(): IPresenter<AppState, IView> = MainPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_layout)
        binding?.fab?.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    presenter.getData(searchWord, true)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun renderData(appState: AppState) {
        binding?.let {
            when (appState) {
                is AppState.Success -> {
                    val dataModel = appState.data
                    if (dataModel == null || dataModel.isEmpty()) {
                        showErrorScreen(getString(R.string.empty_server_response_on_success))
                    } else {
                        showViewSuccess()
                        if (adapter == null) {
                            it.mainRv.layoutManager = LinearLayoutManager(applicationContext)
                            it.mainRv.adapter = MainAdapter(onListItemClickListener, dataModel, this)
                        } else {
                            adapter!!.setData(dataModel)
                        }
                    }
                }
                is AppState.Loading -> {
                    showViewLoading()
                }
                is AppState.Error -> {
                    showErrorScreen(appState.error.message)
                }
            }
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        binding?.let {
            it.mainTvError.text = error ?: getString(R.string.undefined_error)
            it.reloadBtn.setOnClickListener {
                presenter.getData(getString(R.string.hi), true)
            }
        }
    }

    private fun showViewSuccess() {
        binding?.let {
            it.mainPb.visibility = GONE
            it.errorLayout.visibility = GONE
            it.mainRv.visibility = VISIBLE
        }
    }

    private fun showViewLoading() {
        binding?.let {
            it.mainPb.visibility = VISIBLE
            it.errorLayout.visibility = GONE
            it.mainRv.visibility = GONE
        }
    }

    private fun showViewError() {
        binding?.let {
            it.mainPb.visibility = GONE
            it.errorLayout.visibility = VISIBLE
            it.mainRv.visibility = GONE
        }
    }

}