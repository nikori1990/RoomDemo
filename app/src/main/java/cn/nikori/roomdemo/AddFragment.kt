package cn.nikori.roomdemo


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add.*

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {

    private lateinit var wordViewModel: WordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel::class.java)

        btn_save.isEnabled = false
        et_english.requestFocus()
        val imm = requireActivity().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.showSoftInput(et_english, 0)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn_save.isEnabled = et_english.text.isNotBlank() && et_chinese.text.isNotBlank()
            }
        }

        et_english.addTextChangedListener(textWatcher)
        et_chinese.addTextChangedListener(textWatcher)

        btn_save.setOnClickListener {
            val word = Word(et_english.text.toString(), et_chinese.text.toString())
            wordViewModel.insertWords(word)
            Navigation.findNavController(it).navigateUp()
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
