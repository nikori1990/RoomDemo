package cn.nikori.roomdemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository

    val allWords: LiveData<List<Word>>

    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(vararg words: Word) = viewModelScope.launch {
        repository.insert(*words)
    }

    fun update(vararg words: Word) = viewModelScope.launch {
        repository.update(*words)
    }

    fun deleteAll() = viewModelScope.launch {
        Log.e("myLog", "update")
        repository.deleteAll()
    }

    fun findWordsWithPattern(pattern: String): LiveData<List<Word>> {
        return repository.findWordsWithPattern(pattern)
    }

    fun delete(word: Word) = viewModelScope.launch {
        repository.delete(word)
    }
}
