package com.badmitry.translater.view.base

import com.badmitry.translator.model.data.AppState

interface IView {

    fun renderData(appState: AppState)

}
