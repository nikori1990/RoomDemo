package cn.nikori.roomdemo

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.cell_normal_2.view.*

class WordItemView(context: Context?, useCardView: Boolean) : RelativeLayout(context) {
    fun setData(data: Word, index: Int) {
        tv_number.text = index.toString()
        tv_english.text = data.word
        tv_chinese.text = data.chineseMeaning

        if (data.chineseInvisible) {
            tv_chinese.visibility = View.GONE
            switch_invisible.isChecked = true
        } else {
            tv_chinese.visibility = View.VISIBLE
            switch_invisible.isChecked = false
        }
    }

    init {
        if (useCardView) {
            View.inflate(context, R.layout.cell_card_2, this)
        } else {
            View.inflate(context, R.layout.cell_normal_2, this)
        }
    }
}