package com.badmitry.translater.model.datasource

import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.datasource.DataSource

interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
}
