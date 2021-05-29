package com.dream_warriors.hunger4none.data.firebase

import android.graphics.Bitmap
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

object FirebaseML
{
    fun classify(image: Bitmap, type: String, onSuccess: (MutableList<Classifications>) -> Unit, onError: () -> Unit)
    {
        val conditions = CustomModelDownloadConditions.Builder().requireWifi().build()
        FirebaseModelDownloader.getInstance().getModel("${type}_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions).addOnSuccessListener {
            val modelFile = it.file
            if (modelFile != null)
            {
                val options = ImageClassifier.ImageClassifierOptions.builder().setMaxResults(1).build()
                val imageClassifier = ImageClassifier.createFromFileAndOptions(modelFile, options)
                val result = imageClassifier.classify(TensorImage.fromBitmap(image))
                onSuccess(result)
            }
            else onError()
        }.addOnFailureListener {
            it.printStackTrace()
            onError()
        }
    }
}