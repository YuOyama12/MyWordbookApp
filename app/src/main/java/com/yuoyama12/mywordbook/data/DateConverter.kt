package com.yuoyama12.mywordbook.data

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun convertDateToLong(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun convertLongToDate(value: Long): Date {
        return Date(value)
    }
}