package cn.nikori.roomdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel

    private val adapter1 by lazy { MyAdapter(false, wordViewModel) }
    private val adapter2 by lazy { MyAdapter(true, wordViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        wordViewModel.getAllWords().observe(this, Observer<List<Word>> {
            Log.e("myLog", "viewModel observe")
            adapter1.setData(it)
            adapter2.setData(it)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter1

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                recyclerView.adapter = adapter2
            } else {
                recyclerView.adapter = adapter1
            }
        }

        btn_insert.setOnClickListener {
            val english = arrayOf(
                "Hello", "World", "Android", "Google",
                "Studio", "Project", "Database", "Recycler", "View",
                "String", "Value", "Integer"
            )

            val chinese = arrayOf(
                "你好", "世界", "安卓", "谷歌",
                "工作室", "项目", "数据库", "回收站", "视图",
                "字符串", "价值", "整数"
            )

            for (i: Int in english.indices) {
                wordViewModel.insertWords(Word(english[i], chinese[i]))
            }
        }

        btn_clear.setOnClickListener {
            wordViewModel.deleteAllWords()
        }

//        btn_update.setOnClickListener {
//            val word = Word("Hi", "你好啊")
//            word.id = 75
//            wordViewModel.updateWords(word)
//        }
//
//        btn_delete.setOnClickListener {
//            val word = Word("Hi", "你好啊")
//            word.id = 80
//            wordViewModel.deleteWords(word)
//        }
    }
}
