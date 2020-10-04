package com.spotolcom.easyrepeater.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "translate") val translate: String = "empty",
    @ColumnInfo(name = "date") val lastName: String = "00:00:00",
    @ColumnInfo(name = "count") val count: Int = 1,
    @ColumnInfo(name = "description") val description: String = "description"

)
