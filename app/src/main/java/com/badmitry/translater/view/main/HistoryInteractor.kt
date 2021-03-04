package com.badmitry.translater.view.main

import com.badmitry.data.AppState
import com.badmitry.data.DataModel
import com.badmitry.repository.datasource.RepositoryLocal
import com.badmitry.repository.repo.Repository
import com.badmitry.translator.viewmodel.Interactor

class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}
