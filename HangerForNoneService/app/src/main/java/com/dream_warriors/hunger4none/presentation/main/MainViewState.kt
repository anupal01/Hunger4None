package com.dream_warriors.hunger4none.presentation.main

sealed class MainViewState
{
    class DataState(val classification: String, val accuracy: Float) : MainViewState()
    object FeedbackSubmittedState : MainViewState()
    object ErrorState : MainViewState()
    object LoadingState : MainViewState()
}