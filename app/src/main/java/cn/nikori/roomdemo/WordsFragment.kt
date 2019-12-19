package cn.nikori.roomdemo


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_words.*

/**
 * A simple [Fragment] subclass.
 */
class WordsFragment : Fragment() {

    private val VIEW_TYPE: String = "view_type"
    private val IS_USING_CARD_VIEW: String = "is_using_card_view"

    private lateinit var wordViewModel: WordViewModel

    private val adapter1 by lazy { MyAdapter(false, wordViewModel) }
    private val adapter2 by lazy { MyAdapter(true, wordViewModel) }

    private lateinit var filteredWords: LiveData<List<Word>>

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel::class.java)
        filteredWords = wordViewModel.getAllWords()
        filteredWords.observe(requireActivity(), Observer<List<Word>> {
            val temp = adapter1.itemCount
            adapter1.setData(it)
            adapter2.setData(it)
            if (temp != it.size) {
//                adapter1.notifyDataSetChanged()
//                adapter2.notifyDataSetChanged()
                // 平滑
                adapter1.notifyItemInserted(0)
                adapter2.notifyItemInserted(0)
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val shp = requireActivity().getSharedPreferences(VIEW_TYPE, Context.MODE_PRIVATE)
        val viewType = shp.getBoolean(IS_USING_CARD_VIEW, false)
        if (viewType) {
            recyclerView.adapter = adapter2
        } else {
            recyclerView.adapter = adapter1
        }

        floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_wordsFragment_to_addFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        val imm = requireActivity().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_data -> {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("清空数据")
                builder.setPositiveButton(
                    "确定"
                ) { _, _ -> wordViewModel.deleteAllWords() }
                builder.setNegativeButton(
                    "取消"
                ) { _, _ -> }
                builder.create()
                builder.show()
            }
            R.id.menu_switch_view -> {
                val shp = requireActivity().getSharedPreferences(VIEW_TYPE, Context.MODE_PRIVATE)
                shp.getBoolean(IS_USING_CARD_VIEW, false)
                with(shp.edit()) {
                    if (recyclerView.adapter == adapter1) {
                        recyclerView.adapter = adapter2
                        this.putBoolean(IS_USING_CARD_VIEW, true)
                    } else {
                        recyclerView.adapter = adapter1
                        this.putBoolean(IS_USING_CARD_VIEW, false)
                    }
                    apply()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth = 750
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { pattern ->
                    filteredWords.removeObservers(requireActivity())
                    filteredWords = wordViewModel.findWordsWithPattern(pattern)
                    filteredWords.observe(requireActivity(), Observer { list ->
                        val temp = adapter1.itemCount
                        adapter1.setData(list)
                        adapter2.setData(list)
                        if (temp != list.size) {
                            adapter1.notifyDataSetChanged()
                            adapter2.notifyDataSetChanged()
                        }
                    })
                }
                return true
            }
        })
    }
}
