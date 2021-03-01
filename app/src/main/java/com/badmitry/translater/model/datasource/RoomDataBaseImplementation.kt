package com.badmitry.translator.model.datasource

import com.badmitry.translater.model.datasource.DataSourceLocal
import com.badmitry.translater.room.HistoryDao
import com.badmitry.translater.room.HistoryEntity
import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel

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

