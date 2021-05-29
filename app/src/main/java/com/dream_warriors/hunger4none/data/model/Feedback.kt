package com.dream_warriors.hunger4none.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Feedback : RealmObject()
{
    @PrimaryKey
    var date: Long = 0
    var plantType: String = ""
    var classification: String = ""
    var accuracy: Float = 0f
    var rate: Int = 0
    var feedback: String = ""
}