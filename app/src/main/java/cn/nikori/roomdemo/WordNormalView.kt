package cn.nikori.roomdemo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.cell_normal_2.view.*

class WordNormalView : RelativeLayout {
    fun setData(current: Word, index: Int) {
        tv_number.text = index.toString()
        tv_english.text = current.word
        tv_chinese.text = current.chineseMeaning

        if (current.chineseInvisible) {
            tv_chinese.visibility = View.GONE
            switch_invisible.isChecked = true
        } else {
            tv_chinese.visibility = View.VISIBLE
            switch_invisible.isChecked = false
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.cell_normal_2, this)
    }
}