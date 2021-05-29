package com.dream_warriors.hunger4none.domain.repository

import com.dream_warriors.hunger4none.data.model.Feedback

interface FeedbackRepository
{
    suspend fun submitFeedback(feedback: Feedback, onFinished: (Boolean) -> Unit)
}