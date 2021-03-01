package com.badmitry.translater.view.main

import com.badmitry.translator.model.data.AppState
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.model.data.Meanings

fun parseSearchResults(data: AppState): AppState {
    val newSearchResults = arrayListOf<DataModel>()
    when (data) {
        is AppState.Success -> {
            val searchResults = data.data
            if (!searchResults.isNullOrEmpty()) {
                for (searchResult in searchResults) {
                    parseResult(searchResult, newSearchResults)
                }
            }
        }
    }

    return AppState.Success(newSearchResults)
}

private fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>) {
    if (!dataModel.text.isNullOrBlank() && !dataModel.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in dataModel.meanings) {
            if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
                newMeanings.add(Meanings(meaning.translation, meaning.imageUrl))
            }
        }
        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.text, newMeanings))
        }
    }
}

fun convertMeaningsToString(meanings: List<Meanings>): String {
    var meaningsSeparatedByComma = String()
    for ((index, meaning) in meanings.withIndex()) {
        meaningsSeparatedByComma += if (index + 1 != meanings.size) {
            String.format("%s%s", meaning.translation?.translation, ", ")
        } else {
            meaning.translation?.translation
        }
    }
    return meaningsSeparatedByComma
}

fun parseLocalSearchResults(appState: AppState): AppState {
    return AppState.Success(mapResult(appState, false))
}

private fun mapResult(
    state: AppState,
    isOnline: Boolean
): List<DataModel> {
    val newSearchResults = arrayListOf<DataModel>()
    when (state) {
        is AppState.Success -> {
            getSuccessResultData(state, isOnline, newSearchResults)
        }
    }
    return newSearchResults
}

private fun getSuccessResultData(
    state: AppState.Success,
    isOnline: Boolean,
    newSearchResults: ArrayList<DataModel>
) {
    val searchResults: List<DataModel> = state.data as List<DataModel>
    if (searchResults.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in searchResults) {
                parseOnlineResult(searchResult, newSearchResults)
            }
        } else {
            for (searchResult in searchResults) {
                newSearchResults.add(DataModel(searchResult.text, arrayListOf()))
            }
        }
    }
}

private fun parseOnlineResult(searchResult: DataModel, newSearchResults: ArrayList<DataModel>) {
    if (!searchResult.text.isNullOrBlank() && !searchResult.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in searchResult.meanings) {
            if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
                newMeanings.add(Meanings(meaning.translation, meaning.imageUrl))
            }
        }
        if (newMeanings.isNotEmpty()) {
            newSearchResults.add(DataModel(searchResult.text, newMeanings))
        }
    }
}

