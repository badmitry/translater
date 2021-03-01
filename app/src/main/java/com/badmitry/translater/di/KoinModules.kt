package com.badmitry.translater.di

import androidx.room.Room
import com.badmitry.translater.model.datasource.RepoImplLocal
import com.badmitry.translater.model.datasource.RepositoryLocal
import com.badmitry.translater.room.DataBase
import com.badmitry.translater.view.history.HistoryViewModel
import com.badmitry.translater.view.main.HistoryInteractor
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
    single { Room.databaseBuilder(get(), DataBase::class.java, "HistoryDB").build() }
    single { get<DataBase>().getDao() }

    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> { RepoImplLocal(RoomDataBaseImplementation(get())) }
}

val mainScreen = module {
    factory { MainInteractor(get(), get())}
    factory { MainViewModel(get()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}
