package cn.nikori.roomdemo

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {
    val allWords: LiveData<List<Word>> = wordDao.loadWords()

    suspend fun insert(vararg words: Word) {
        wordDao.insert(*words)
    }

    suspend fun update(vararg words: Word) {
        wordDao.update(*words)
    }

    suspend fun delete(word: Word) {
        wordDao.delete(word)
    }

    suspend fun deleteAll() {
        wordDao.deleteAll()
    }

    fun findWordsWithPattern(pattern: String): LiveData<List<Word>> {
        return wordDao.findWordsWithPattern("%$pattern%")
    }
}