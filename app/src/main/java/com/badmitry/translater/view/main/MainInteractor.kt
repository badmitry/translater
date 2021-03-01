package com.badmitry.translater.view.main

import com.badmitry.translater.model.datasource.RepositoryLocal
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.model.repository.Repository
import com.badmitry.translator.viewmodel.Interactor

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState: AppState
        if (fromRemoteSource) {
            appState = AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = AppState.Success(repositoryLocal.getData(word))
        }
        return appState
    }

}
