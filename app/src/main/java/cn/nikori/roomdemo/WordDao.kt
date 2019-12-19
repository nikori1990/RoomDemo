package cn.nikori.roomdemo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWords(vararg words: Word)

    @Insert
    fun insertBothWords(word1: Word, word2: Word)

    @Insert
    fun insertWordAndWords(word: Word, words: List<Word>)

    @Update
    fun updateWords(vararg words: Word)

    @Delete
    fun deleteWords(vararg words: Word)

    @Query("DELETE FROM word")
    fun deleteAllWords()

    @Query("SELECT * FROM word ORDER BY ID DESC")
    fun loadWords(): LiveData<List<Word>>

    @Query("SELECT * FROM word WHERE id > :minId")
    fun loadAllWordsBiggerThan(minId: Int): LiveData<List<Word>>

    @Query("SELECT * FROM word WHERE english_word LIKE :search")
    fun findWordsWithEnglishName(search: String): LiveData<List<Word>>

    @Query("SELECT * FROM word ORDER BY id DESC")
    fun getAllWordsLive(): LiveData<List<Word>>

    @Query("SELECT * FROM word WHERE english_word LIKE :pattern ORDER BY ID DESC")
    fun findWordsWithPattern(pattern: String): LiveData<List<Word>>
}