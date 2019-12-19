package cn.nikori.roomdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val wordRepository by lazy { WordRepository(application) }

    fun getAllWords(): LiveData<List<Word>> {
        return wordRepository.getAllWords()
    }

    fun findWordsWithPattern(pattern: String): LiveData<List<Word>> {
        return wordRepository.findWordsWithPattern(pattern)
    }

    fun insertWords(vararg words: Word) {
        wordRepository.insertWords(*words)
    }

    fun updateWords(vararg words: Word) {
        wordRepository.updateWords(*words)
    }

    fun deleteWords(vararg words: Word) {
        wordRepository.deleteWords(*words)
    }

    fun deleteAllWords() {
        wordRepository.deleteAllWords()
    }
}