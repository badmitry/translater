package com.badmitry.translater.model.datasource

import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel

class RepoImplLocal (private val dataSource: DataSourceLocal<List<DataModel>>) :
    RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}
