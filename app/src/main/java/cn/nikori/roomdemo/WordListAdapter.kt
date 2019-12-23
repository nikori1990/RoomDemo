package cn.nikori.roomdemo

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_normal_2.view.*

class WordListAdapter internal constructor(
    private val useCardView: Boolean,
    private val wordViewModel: WordViewModel
) :
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private var words = emptyList<Word>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val viewHolder = WordViewHolder(WordItemView(parent.context, useCardView))
        val itemView = viewHolder.itemView as WordItemView

        itemView.setOnClickListener {
            val uri = Uri.parse(
                "https://m.youdao.com/dict?le=eng&q=" +
                        viewHolder.itemView.tv_english.text
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            it.context.startActivity(intent)
        }

        itemView.switch_invisible.setOnCheckedChangeListener { _, isChecked ->
            val data = itemView.getTag(R.id.word_for_view_holder) as Word
            val position = itemView.getTag(R.id.position_for_view_holder) as Int
            data.chineseInvisible = isChecked
            wordViewModel.update(data)
            viewHolder.itemView.setData(data, position + 1)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val data = words[position]
        val itemView = holder.itemView as WordItemView

        itemView.setTag(R.id.word_for_view_holder, data)
        itemView.setTag(R.id.position_for_view_holder, position)

        itemView.setData(data, position + 1)
    }

    internal fun setWords(words: List<Word>) {
        this.words = words
    }

    override fun getItemCount() = words.size
}