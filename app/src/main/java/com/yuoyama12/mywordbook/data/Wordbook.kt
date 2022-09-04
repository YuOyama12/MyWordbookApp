package com.yuoyama12.mywordbook.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Wordbook(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    val id: Long = 0,
    val name: String,
    val modifiedDate: Date = Date(System.currentTimeMillis())
): Parcelable