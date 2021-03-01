package com.badmitry.translater.view.main

import com.badmitry.translater.model.datasource.RepositoryLocal
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.model.repository.Repository
import com.badmitry.translator.viewmodel.Interactor

class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState{
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}
