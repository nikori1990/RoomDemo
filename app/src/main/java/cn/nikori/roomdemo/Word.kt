package cn.nikori.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
class Word(
    @ColumnInfo(name = "english_word")
    val word: String,

    @ColumnInfo(name = "chinese_meaning")
    val chineseMeaning: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "chinese_invisible")
    var chineseInvisible: Boolean = false
}