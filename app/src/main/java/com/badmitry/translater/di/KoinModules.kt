package com.badmitry.translater.di

import androidx.room.Room
import com.badmitry.data.DataModel
import com.badmitry.repository.datasource.RepoImplLocal
import com.badmitry.repository.datasource.RepositoryLocal
import com.badmitry.repository.datasource.RetrofitImplementation
import com.badmitry.repository.datasource.RoomDataBaseImplementation
import com.badmitry.repository.repo.Repository
import com.badmitry.repository.repo.RepositoryImplementation
import com.badmitry.repository.room.DataBase
import com.badmitry.translater.view.history.HistoryViewModel
import com.badmitry.translater.view.main.HistoryInteractor
import com.badmitry.translater.view.main.MainInteractor
import com.badmitry.translater.view.main.MainViewModel
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
