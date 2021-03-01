package com.badmitry.translater.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.badmitry.translater.R
import com.badmitry.translater.databinding.MainLayoutBinding
import com.badmitry.translater.view.base.BaseActivity
import com.badmitry.translater.view.base.isOnline
import com.badmitry.translater.view.descriptionscreen.DescriptionActivity
import com.badmitry.translater.view.history.HistoryActivity
import com.badmitry.translater.view.main.adapter.MainAdapter
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<AppState, MainInteractor>() {

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }

    override lateinit var model: MainViewModel
    private var binding: MainLayoutBinding? = null
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener, this) }
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                startActivity(
                    DescriptionActivity.getIntent(
                        this@MainActivity,
                        data.text!!,
                        convertMeaningsToString(data.meanings!!),
                        data.meanings[0].imageUrl
                    )
                )
            }
        }

    private val fabClickListener: View.OnClickListener =
        View.OnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

    private val onSearchClickListener: SearchDialogFragment.OnSearchClickListener =
        object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    model.getData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_layout)
        iniViewModel()
        binding?.fab?.setOnClickListener(fabClickListener)
        binding?.let {
            it.mainRv.layoutManager = LinearLayoutManager(applicationContext)
            it.mainRv.adapter = adapter
        }
    }

    private fun iniViewModel() {
        if (binding?.mainRv?.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@MainActivity, Observer<AppState> { renderData(it) })
    }

    override fun showViewSuccess() {
        binding?.let {
            it.mainPb.visibility = GONE
            it.mainRv.visibility = VISIBLE
        }
    }

    override fun showViewLoading() {
        binding?.let {
            it.mainPb.visibility = VISIBLE
            it.mainRv.visibility = GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }
}