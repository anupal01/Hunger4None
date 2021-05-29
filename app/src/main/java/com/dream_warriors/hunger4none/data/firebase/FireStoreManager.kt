package com.dream_warriors.hunger4none.data.firebase

import com.dream_warriors.hunger4none.data.model.Feedback
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreManager
{
    fun submitFeedback(feedback: Feedback, onFinished: (Boolean) -> Unit)
    {
        val db = FirebaseFirestore.getInstance()
        val feedbacks = db.collection("feedbacks")
        val data = hashMapOf(
            "date" to feedback.date,
            "corp_analysed" to feedback.plantType,
            "analysed_result" to feedback.classification,
            "accuracy_percentage" to feedback.accuracy,
            "rate" to feedback.rate,
            "feedback" to feedback.feedback
        )
        feedbacks.document().set(data).addOnSuccessListener {
            onFinished(true)
        }.addOnFailureListener {
            it.printStackTrace()
            onFinished(false)
        }
    }
}