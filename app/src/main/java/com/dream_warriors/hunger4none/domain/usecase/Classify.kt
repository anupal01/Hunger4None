package com.dream_warriors.hunger4none.domain.usecase

import android.graphics.Bitmap
import com.dream_warriors.hunger4none.data.firebase.FirebaseML
import com.dream_warriors.hunger4none.utils.Constants

class Classify
{
    fun run(image: Bitmap, type: Int, onFinished: (String, Float) -> Unit, onError: () -> Unit)
    {
        FirebaseML.classify(image, Constants.models[type], { classifications ->
            if (!classifications.isNullOrEmpty())
            {
                val classification = classifications.first()
                if (!classification.categories.isNullOrEmpty())
                {
                    classification.categories.sortBy { category -> category.score }
                    val category = classification.categories.last()
                    onFinished(category.label, category.score)
                }
                else onError()
            }
            else onError()
        }, onError)
    }
}