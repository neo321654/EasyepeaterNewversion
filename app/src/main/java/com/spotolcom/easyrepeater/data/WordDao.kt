package com.spotolcom.easyrepeater.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spotolcom.easyrepeater.data.Word


@Dao
interface WordDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from word_table ORDER BY id DESC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    @Query("SELECT * from word_table ORDER BY random() ")
    suspend fun getRandWords(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Query("UPDATE word_table SET word = :word,translate = :translate WHERE id = :id")
    fun update(id:String, word: String,translate:String )

    @Query("DELETE FROM word_table")
    fun deleteAll()
}
