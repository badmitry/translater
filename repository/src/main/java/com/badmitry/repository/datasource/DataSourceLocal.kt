package com.badmitry.repository.datasource

import com.badmitry.data.AppState

interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
}
