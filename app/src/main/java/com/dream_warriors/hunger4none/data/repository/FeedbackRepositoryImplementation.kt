package com.dream_warriors.hunger4none.data.repository

import com.dream_warriors.hunger4none.data.firebase.FireStoreManager
import com.dream_warriors.hunger4none.data.model.Feedback
import com.dream_warriors.hunger4none.data.realm.Database
import com.dream_warriors.hunger4none.domain.repository.FeedbackRepository

class FeedbackRepositoryImplementation constructor(private val database: Database) : FeedbackRepository
{
    override suspend fun submitFeedback(feedback: Feedback, onFinished: (Boolean) -> Unit)
    {
        FireStoreManager.submitFeedback(feedback) {
            if (it) database.insert(feedback)
            onFinished(it)
        }
    }
}