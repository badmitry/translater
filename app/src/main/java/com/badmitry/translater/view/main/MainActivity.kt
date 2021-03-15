package com.badmitry.translater.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.badmitry.data.AppState
import com.badmitry.data.DataModel
import com.badmitry.translater.R
import com.badmitry.translater.databinding.MainLayoutBinding
import com.badmitry.translater.di.injectDependencies
import com.badmitry.translater.view.base.BaseActivity
import com.badmitry.translater.view.base.isOnline
//import com.badmitry.translater.view.descriptionscreen.DescriptionActivity
import com.badmitry.translater.view.history.HistoryActivity
import com.badmitry.translater.view.main.adapter.MainAdapter
import com.badmitry.translater.view.viewById
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

private const val DESCRIPTION_ACTIVITY_PATH = "com.badmitry.dynamicfeaturedescription.descriptionscreen.DescriptionActivity"
private const val DESCRIPTION_ACTIVITY_FEATURE_NAME = "dynamicfeaturedescription"
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
        const val WORD_EXTRA = "f76a288a-5dcc-43f1-ba89-7fe1d53f63b0"
        const val DESCRIPTION_EXTRA = "0eeb92aa-520b-4fd1-bb4b-027fbf963d9a"
        const val URL_EXTRA = "6e4b154d-e01f-4953-a404-639fb3bf7281"
    }

    private val searchFab by viewById<FloatingActionButton>(R.id.fab)

    override lateinit var model: MainViewModel
    private lateinit var splitInstallManager: SplitInstallManager
    private var binding: MainLayoutBinding? = null
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener, this) }
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
                val request =
                    SplitInstallRequest
                        .newBuilder()
                        .addModule(DESCRIPTION_ACTIVITY_FEATURE_NAME)
                        .build()

                splitInstallManager
                    .startInstall(request)
                    .addOnSuccessListener {
                        val intent = Intent().setClassName(packageName, DESCRIPTION_ACTIVITY_PATH).apply {
                            putExtra(WORD_EXTRA, data.text!!)
                            putExtra(DESCRIPTION_EXTRA, convertMeaningsToString(data.meanings!!))
                            putExtra(URL_EXTRA, data.meanings!![0].imageUrl)
                        }
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't download feature: " + it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
//                true
//                startActivity(
//                    DescriptionActivity.getIntent(
//                        this@MainActivity,
//                        data.text!!,
//                        convertMeaningsToString(data.meanings!!),
//                        data.meanings!![0].imageUrl
//                    )
//                )
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
        searchFab.setOnClickListener(fabClickListener)
        binding?.let {
            it.mainRv.layoutManager = LinearLayoutManager(applicationContext)
            it.mainRv.adapter = adapter
        }
    }

    private fun iniViewModel() {
        if (binding?.mainRv?.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        injectDependencies()
        val viewModel: MainViewModel by currentScope.inject()
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