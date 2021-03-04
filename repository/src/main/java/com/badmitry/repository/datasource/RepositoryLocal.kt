package com.badmitry.repository.datasource

import com.badmitry.data.AppState
import com.badmitry.repository.repo.Repository

interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}
