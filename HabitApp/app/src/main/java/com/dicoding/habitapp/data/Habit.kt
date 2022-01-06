package com.dicoding.habitapp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

//TODO 1 : Define a local database table using the schema in app/schema/habits.json
@Entity(tableName = "habits")
@Parcelize
data class Habit(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @NotNull
    @ColumnInfo(name = "title")
    val title: String,

    @NotNull
    @ColumnInfo(name = "minutesFocus")
    val minutesFocus: Long,

    @NotNull
    @ColumnInfo(name = "startTime")
    val startTime: String,

    @NotNull
    @ColumnInfo(name = "priorityLevel")
    val priorityLevel: String
) : Parcelable
