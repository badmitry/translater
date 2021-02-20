package com.badmitry.translater.view.main

import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.model.repository.Repository
import com.badmitry.translator.viewmodel.Interactor
import io.reactivex.Observable

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                remoteRepository
            } else {
                localRepository
            }.getData(word)
        )
    }
}
