package com.app.todo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.app.todo.data.DateTypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Abstract table.
 * Each argument of class is a column inside task_table.
 */
@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val priority: String,
    val date: Calendar,
) : Parcelable