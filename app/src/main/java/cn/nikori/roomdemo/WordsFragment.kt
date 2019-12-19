package cn.nikori.roomdemo


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_words.*

/**
 * A simple [Fragment] subclass.
 */
class WordsFragment : Fragment() {

    private lateinit var wordViewModel: WordViewModel

    private val adapter1 by lazy { MyAdapter(false, wordViewModel) }
    private val adapter2 by lazy { MyAdapter(true, wordViewModel) }

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
        wordViewModel.getAllWords().observe(this, Observer<List<Word>> {
            val temp = adapter1.itemCount
            adapter1.setData(it)
            adapter2.setData(it)
            if (temp != it.size) {
                adapter1.notifyDataSetChanged()
                adapter2.notifyDataSetChanged()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter1

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
}
