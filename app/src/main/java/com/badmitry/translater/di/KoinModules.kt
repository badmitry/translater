package com.badmitry.translater.di

import com.badmitry.translater.view.main.MainInteractor
import com.badmitry.translater.view.main.MainViewModel
import com.badmitry.translator.model.data.DataModel
import com.badmitry.translator.model.datasource.RetrofitImplementation
import com.badmitry.translator.model.datasource.RoomDataBaseImplementation
import com.badmitry.translator.model.repository.Repository
import com.badmitry.translator.model.repository.RepositoryImplementation
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) { RepositoryImplementation(
        RetrofitImplementation()
    ) }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) { RepositoryImplementation(
        RoomDataBaseImplementation()
    ) }
}

val mainScreen = module {
    factory { MainInteractor(get(named(NAME_REMOTE)), get(named(NAME_LOCAL))) }
    factory { MainViewModel(get()) }
}