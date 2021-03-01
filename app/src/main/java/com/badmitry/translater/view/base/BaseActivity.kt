package com.badmitry.translater.view.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.badmitry.translater.R
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.view.main.AlertDialogFragment
import com.badmitry.translator.viewmodel.BaseViewModel
import com.badmitry.translator.viewmodel.Interactor

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "1100"
    }

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkAvailable = isOnline(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        isNetworkAvailable = isOnline(applicationContext)
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message).show(supportFragmentManager, DIALOG_FRAGMENT_TAG)
    }

    private fun isDialogNull(): Boolean {
        return supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    abstract fun setDataToAdapter(data: List<DataModel>)

    protected fun renderData(appState: T) {
        when (appState) {
            is AppState.Success -> {
                showViewSuccess()
                appState.data?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
            }
            is AppState.Error -> {
                showViewSuccess()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    abstract fun showViewSuccess()

    abstract fun showViewLoading()

}