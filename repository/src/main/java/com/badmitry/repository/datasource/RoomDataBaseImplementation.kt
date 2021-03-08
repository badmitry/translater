package com.badmitry.repository.datasource

import com.badmitry.data.AppState
import com.badmitry.data.DataModel
import com.badmitry.repository.room.HistoryDao
import com.badmitry.repository.room.HistoryEntity

class RoomDataBaseImplementation(private val historyDao: HistoryDao) :
    DataSourceLocal<List<DataModel>> {
    override suspend fun getData(word: String): List<DataModel> {
        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }

    fun convertDataModelSuccessToEntity(appState: AppState) : HistoryEntity {
        if (appState is AppState.Success) {
            return HistoryEntity(appState.data?.get(0)?.text?:"word",null)
        } else {
            return HistoryEntity("word", null)
        }
    }

    fun mapHistoryEntityToSearchResult(list: List<HistoryEntity>): List<DataModel> {
        val dataModel= ArrayList<DataModel>()
        if (!list.isNullOrEmpty()) {
            for (entity in list) {
                dataModel.add(DataModel(entity.word, null))
            }
        }
        return dataModel
    }
}

