package com.badmitry.translater.model.datasource

import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.repository.Repository

interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}
