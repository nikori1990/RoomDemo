package cn.nikori.roomdemo

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_normal_2.view.*

class MyAdapter(private val userCardView: Boolean, private val wordViewModel: WordViewModel) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var list = ArrayList<Word>()

    fun setData(data: List<Word>) {
        list.clear()
        list.addAll(data)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myViewHolder = MyViewHolder(MyView(parent.context, userCardView))
        val itemView = myViewHolder.itemView as MyView

        itemView.setOnClickListener {
            val uri = Uri.parse(
                "https://m.youdao.com/dict?le=eng&q=" +
                        myViewHolder.itemView.tv_english.text
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            it.context.startActivity(intent)
        }

        itemView.switch_invisible.setOnCheckedChangeListener { _, isChecked ->
            val data = itemView.getTag(R.id.word_for_view_holder) as Word
            val position = itemView.getTag(R.id.position_for_view_holder) as Int
            data.chineseInvisible = isChecked
            wordViewModel.updateWords(data)
            myViewHolder.itemView.setData(data, position + 1)
        }

        return myViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        val itemView = holder.itemView as MyView

        itemView.setTag(R.id.word_for_view_holder, data)
        itemView.setTag(R.id.position_for_view_holder, position)

        itemView.setData(data, position + 1)
    }

    private var listener: ((word: Word) -> Unit)? = null

    fun setMyListener(listener: (word: Word) -> Unit) {
        this.listener = listener
    }
}