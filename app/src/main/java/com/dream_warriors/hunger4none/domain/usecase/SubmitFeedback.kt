package com.dream_warriors.hunger4none.domain.usecase

import com.dream_warriors.hunger4none.data.model.Feedback
import com.dream_warriors.hunger4none.domain.repository.FeedbackRepository
import com.dream_warriors.hunger4none.utils.Constants
import java.util.*

class SubmitFeedback constructor(private val repository: FeedbackRepository)
{
    suspend fun run(type: Int, classification: String, accuracy: Float, rate: Int, comment: String, onFinished: (Boolean) -> Unit)
    {
        val feedback = Feedback()
        feedback.date = Date().time
        feedback.plantType = Constants.models[type]
        feedback.classification = classification
        feedback.accuracy = accuracy
        feedback.rate = rate
        feedback.feedback = comment
        repository.submitFeedback(feedback, onFinished)
    }
}