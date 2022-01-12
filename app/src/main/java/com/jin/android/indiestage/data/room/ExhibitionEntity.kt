package com.jin.android.indiestage.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jin.android.indiestage.data.firestore.Exhibition


@Entity(tableName = "exhibition")
data class ExhibitionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val image: String,
    val description: String,
    var isCheckedIn: Boolean,
    var isBookMarked: Boolean
) {
    constructor() : this("temp", "title", "image", "description", false, false)
    constructor(exhibition: Exhibition) : this(
        exhibition.id,
        exhibition.title,
        exhibition.image,
        exhibition.description,
        false,
        false
    )
}