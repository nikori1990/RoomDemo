package cn.nikori.roomdemo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_words.*

/**
 * A simple [Fragment] subclass.
 */
class WordsFragment : Fragment() {

    private val VIEW_TYPE: String = "view_type"
    private val IS_USING_CARD_VIEW: String = "is_using_card_view"

    private lateinit var wordViewModel: WordViewModel

    private lateinit var listAdapter1: WordListAdapter
    private lateinit var listAdapter2: WordListAdapter

    private lateinit var filteredWords: LiveData<List<Word>>

    private lateinit var allWords: List<Word>

    private lateinit var dividerItemDecoration: DividerItemDecoration

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

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        listAdapter1 = WordListAdapter(false, wordViewModel)
        listAdapter2 = WordListAdapter(true, wordViewModel)

        filteredWords = wordViewModel.allWords
        filteredWords.observe(viewLifecycleOwner, Observer { words ->
            val temp = listAdapter1.itemCount
            allWords = words
            listAdapter1.setWords(words)
            listAdapter2.setWords(words)
            if (temp != words.size) {
                listAdapter1.notifyItemInserted(0)
                listAdapter2.notifyItemInserted(0)
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val shp = requireActivity().getSharedPreferences(VIEW_TYPE, Context.MODE_PRIVATE)
        val viewType = shp.getBoolean(IS_USING_CARD_VIEW, false)

        dividerItemDecoration =
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)

        if (viewType) {
            recyclerView.adapter = listAdapter2
            recyclerView.removeItemDecoration(dividerItemDecoration)
        } else {
            recyclerView.adapter = listAdapter1
            recyclerView.addItemDecoration(dividerItemDecoration)

        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val word = allWords[viewHolder.adapterPosition]
                Log.e("myLog", word.toString())
                wordViewModel.delete(word)
//                Snackbar.make(this, "删除了一个词汇", Snackbar.LENGTH_SHORT)
                Snackbar.make(
                    requireView().findViewById(R.id.wordsFragmentLayout),
                    "删除了一条数据",
                    Snackbar.LENGTH_SHORT
                ).setAction("撤销", {
                    wordViewModel.insert(word)
                }).show()
            }
        }).attachToRecyclerView(recyclerView)

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
                ) { _, _ -> wordViewModel.deleteAll() }
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
                    if (recyclerView.adapter == listAdapter1) {
                        recyclerView.adapter = listAdapter2
                        recyclerView.removeItemDecoration(dividerItemDecoration)
                        this.putBoolean(IS_USING_CARD_VIEW, true)
                    } else {
                        recyclerView.adapter = listAdapter1
                        recyclerView.addItemDecoration(dividerItemDecoration)
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
                    filteredWords.removeObservers(viewLifecycleOwner)
                    filteredWords = wordViewModel.findWordsWithPattern(pattern)
                    filteredWords.observe(viewLifecycleOwner, Observer { list ->
                        val temp = listAdapter1.itemCount
                        allWords = list
                        listAdapter1.setWords(list)
                        listAdapter2.setWords(list)
                        if (temp != list.size) {
                            listAdapter1.notifyDataSetChanged()
                            listAdapter2.notifyDataSetChanged()
                        }
                    })
                }
                return true
            }
        })
    }
}
