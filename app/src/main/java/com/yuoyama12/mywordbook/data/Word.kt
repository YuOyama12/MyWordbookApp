package com.yuoyama12.mywordbook.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Wordbook::class,
            parentColumns = ["id"],
            childColumns = ["wordbook_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    val id: Long = 0,
    @ColumnInfo(name = "wordbook_id", index = true)
    val wordbookId: Long,
    val word: String,
    val meaning: String,
    val miscellaneousNote: String = "",
    val modifiedDate: Date = Date(System.currentTimeMillis())
) : Parcelable
