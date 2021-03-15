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
import com.badmitry.translater.view.history.HistoryActivity
import com.badmitry.translater.view.history.HistoryViewModel
import com.badmitry.translater.view.main.HistoryInteractor
import com.badmitry.translater.view.main.MainActivity
import com.badmitry.translater.view.main.MainInteractor
import com.badmitry.translater.view.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun injectDependencies() = loadModules

private val loadModules by lazy {
    loadKoinModules(listOf(application, mainScreen, historyScreen))
}

val application = module {
    single { Room.databaseBuilder(get(), DataBase::class.java, "HistoryDB").build() }
    single { get<DataBase>().getDao() }

    single<Repository<List<DataModel>>> {
        RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> {
        RepoImplLocal(RoomDataBaseImplementation(get())) }
}

//val mainScreen = module {
//    factory { MainInteractor(get(), get())}
//    factory { MainViewModel(get()) }
//}

val mainScreen = module {
    scope(named<MainActivity>()) {
        scoped { MainInteractor(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}

val historyScreen = module {
    scope(named<HistoryActivity>()) {
        scoped { HistoryInteractor(get(), get()) }
        viewModel { HistoryViewModel(get()) }
    }
}


//val historyScreen = module {
//    factory { HistoryViewModel(get()) }
//    factory { HistoryInteractor(get(), get()) }
//}
