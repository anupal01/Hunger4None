package com.dream_warriors.hunger4none.application

import com.dream_warriors.hunger4none.data.realm.Database
import com.dream_warriors.hunger4none.data.repository.*
import com.dream_warriors.hunger4none.domain.repository.*
import com.dream_warriors.hunger4none.domain.usecase.*
import com.dream_warriors.hunger4none.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single { Database() }

    single { FeedbackRepositoryImplementation(get()) as FeedbackRepository }

    single { Classify() }

    single { SubmitFeedback(get()) }

    viewModel { MainViewModel(get(), get()) }
}