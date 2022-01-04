package com.jin.android.indiestage.data.checkedin

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jin.android.indiestage.data.Exhibition


@Entity(tableName = "checked_in")
data class CheckedInEntity(
    @PrimaryKey val id: String,
    val title: String,
    val image: String,
    val description: String
) {
    constructor() : this("", "", "", "")
    constructor(exhibition: Exhibition) : this(
        exhibition.id,
        exhibition.title,
        exhibition.image,
        exhibition.description
    )
}