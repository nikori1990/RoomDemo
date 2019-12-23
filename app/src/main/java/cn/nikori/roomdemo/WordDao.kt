package cn.nikori.roomdemo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Query("SELECT * FROM word WHERE english_word LIKE :pattern ORDER BY ID DESC")
    fun findWordsWithPattern(pattern: String): LiveData<List<Word>>

    @Query("SELECT * FROM word ORDER BY english_word ASC")
    fun loadWords(): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg words: Word)

    @Update
    suspend fun update(vararg words: Word)

    @Delete
    suspend fun delete(vararg words: Word)

}