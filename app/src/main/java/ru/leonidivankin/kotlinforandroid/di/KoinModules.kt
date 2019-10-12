package ru.leonidivankin.kotlinforandroid.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.provider.FireStoreProvider
import ru.leonidivankin.kotlinforandroid.data.provider.RemoteDataProvider
import ru.leonidivankin.kotlinforandroid.ui.main.MainViewModel
import ru.leonidivankin.kotlinforandroid.ui.note.NoteViewModel
import ru.leonidivankin.kotlinforandroid.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}