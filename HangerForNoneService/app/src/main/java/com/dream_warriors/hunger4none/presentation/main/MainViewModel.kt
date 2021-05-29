package com.dream_warriors.hunger4none.presentation.main

import android.graphics.BitmapFactory
import com.dream_warriors.hunger4none.domain.usecase.Classify
import com.dream_warriors.hunger4none.domain.usecase.SubmitFeedback
import com.dream_warriors.hunger4none.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel constructor(private val classify: Classify, private val submitFeedback: SubmitFeedback) : BaseViewModel<MainViewState>()
{
    var type = -1
        set(value)
        {
            println(value)
            field = value
        }

    var imagePath = ""
        set(value)
        {
            println(value)
            field = value
        }

    fun classify() = CoroutineScope(Dispatchers.IO).launch {
        classify.run(BitmapFactory.decodeFile(imagePath), type, { classification, accuracy ->
            state.postValue(MainViewState.DataState(classification, accuracy))
        }, {
            state.postValue(MainViewState.ErrorState)
        })
    }

    fun submitFeedback(classification: String, accuracy: Float, rate: Int, feedback: String) = CoroutineScope(Dispatchers.IO).launch {
        submitFeedback.run(type, classification, accuracy, rate, feedback) {
            if (it) state.postValue(MainViewState.FeedbackSubmittedState)
            else state.postValue(MainViewState.ErrorState)
        }
    }
}