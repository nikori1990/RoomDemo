package cn.nikori.roomdemo

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userCardView: Boolean) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var list = ArrayList<Word>()

    fun setData(data: List<Word>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MyView(parent.context, userCardView))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        val itemView = holder.itemView as MyView

        holder.itemView.setOnClickListener {
            val uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + data.word)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            it.context.startActivity(intent)
        }
        itemView.setData(data, position + 1)
    }

    private var listener: ((word: Word) -> Unit)? = null

    fun setMyListener(listener: (word: Word) -> Unit) {
        this.listener = listener
    }
}