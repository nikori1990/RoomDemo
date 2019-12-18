package cn.nikori.roomdemo

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class WordRepository(context: Context) {

    private val wordDatabase by lazy { WordDatabase.getDatabase(context.applicationContext) }

    private var wordDao: WordDao

    private var allWordsLive: LiveData<List<Word>>

    init {
        wordDao = wordDatabase.wordDao()
        allWordsLive = wordDao.getAllWordsLive()
    }

    fun getAllWords(): LiveData<List<Word>> {
        return allWordsLive
    }

    fun insertWords(vararg words: Word) {
        InsertAsyncTask(wordDao).execute(*words)
    }

    fun updateWords(vararg words: Word) {
        UpdateAsyncTask(wordDao).execute(*words)
    }

    fun deleteWords(vararg words: Word) {
        DeleteAsyncTask(wordDao).execute(*words)
    }

    fun deleteAllWords() {
        DeleteAllAsyncTask(wordDao).execute()
    }

    class InsertAsyncTask(private val wordDao: WordDao) : AsyncTask<Word, Void, Void>() {
        override fun doInBackground(vararg words: Word?): Void? {
            wordDao.insertWords(*words as Array<out Word>)
            return null
        }
    }

    class UpdateAsyncTask(private val wordDao: WordDao) : AsyncTask<Word, Void, Void>() {
        override fun doInBackground(vararg words: Word?): Void? {
            wordDao.updateWords(*words as Array<out Word>)
            return null
        }
    }

    class DeleteAsyncTask(private val wordDao: WordDao) : AsyncTask<Word, Void, Void>() {
        override fun doInBackground(vararg words: Word?): Void? {
            wordDao.deleteWords(*words as Array<out Word>)
            return null
        }
    }

    class DeleteAllAsyncTask(private val wordDao: WordDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            wordDao.deleteAllWords()
            return null
        }
    }
}