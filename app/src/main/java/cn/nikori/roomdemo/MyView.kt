package cn.nikori.roomdemo

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.cell_normal.view.*

class MyView(context: Context?, useCardView: Boolean) : RelativeLayout(context) {
    fun setData(data: Word, index: Int) {
        tv_number.text = index.toString()
        tv_english.text = data.word
        tv_chinese.text = data.chineseMeaning
    }

    init {
        if (useCardView) {
            View.inflate(context, R.layout.cell_card, this)
        } else {
            View.inflate(context, R.layout.cell_normal, this)
        }
    }
}