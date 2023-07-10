package com.example.oompaloompa.di

import androidx.room.Room
import com.example.oompaloompa.data.database.OompaLoompaDataBase
import com.example.oompaloompa.data.network.OompaLoompaApi
import com.example.oompaloompa.data.repository.CharacterRepository
import com.example.oompaloompa.ui.MainViewModel
import com.example.oompaloompa.ui.detail.DetailActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single<OompaLoompaApi> { OompaLoompaApi.instance }
}

val repositoryModule = module {
    single { CharacterRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailActivityViewModel(get()) }
}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            OompaLoompaDataBase::class.java,
            "oompa_loompa_database"
        ).build()
    }

    single { get<OompaLoompaDataBase>().getOompaLoompaDao() }
}


