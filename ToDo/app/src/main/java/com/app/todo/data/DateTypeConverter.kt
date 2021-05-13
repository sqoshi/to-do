package com.app.todo.data

import androidx.room.TypeConverter
import java.util.*

/***
 * Converts date before database insertion, read.
 */
class DateTypeConverter {
    /**
     * Converts Timestamp to Date object.
     * out of <- database
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        val cal = Calendar.getInstance()
        if (value != null) {
            cal.timeInMillis = value
            return cal
        }
        return null

    }

    /**
     * Converts date to timestamps
     * to -> database
     */
    @TypeConverter
    fun dateToTimestamp(date: Calendar?): Long? {
        return date?.timeInMillis
    }

}