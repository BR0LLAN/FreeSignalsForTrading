package com.devcraft.freesignalsfortrading.presentation.ui.favorite

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.databinding.FragmentFavoriteBinding
import com.devcraft.freesignalsfortrading.presentation.ui.quotes.QuotesViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.pop_up_confirm.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteFragment : Fragment() {
    private val quotesViewModel by sharedViewModel<QuotesViewModel>()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var favoriteAdapter: FavoriteAdapter = FavoriteAdapter()
    private var quote : MutableList<String> = mutableListOf()
    private lateinit var preferencesForFavorite: SharedPreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        dialog = Dialog(requireContext())
        initObservers()
        preferencesForFavorite =  requireContext().getSharedPreferences("favorite", Context.MODE_PRIVATE)
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_fav.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        quoteFill()
        loadDataModel()
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun initObservers() {
        quotesViewModel.quotesCurrencies.observe(viewLifecycleOwner, {
            favoriteAdapter.items = it
        })
        quotesViewModel.getQuotesFailure.observe(this, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun quoteFill(){
        for(value in quotesViewModel.quotesCurrencies.value!!){
            if(preferencesForFavorite.getString(value.symbol,"")!= ""){
                println(preferencesForFavorite.getString(value.symbol,""))
                quote.add(value.symbol)
            }
        }

    }

    private fun  loadDataModel(){
        if(quote.isNotEmpty()){
            quotesViewModel.loadData(quote)
        }
        favoriteAdapter = FavoriteAdapter()
        rv_fav.adapter = favoriteAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.delete_fav -> {
                showDialogConfirm()
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if(Paper.book().contains("symbol")){
            Paper.book().delete("symbol")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogConfirm() {
        dialog.setContentView(R.layout.pop_up_confirm)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialog.positive_btn.setOnClickListener {
            dialog.text_confirm.text = "REMOVE FROM FAVORITES?"
            if(Paper.book().contains("symbolForDeleteFavorite")) {
                preferencesForFavorite.edit()
                    .remove(Paper.book().read("symbolForDeleteFavorite")).apply()
                quote.removeAt(Paper.book().read("positionFavItem"))
                rv_fav.removeViewAt(Paper.book().read("positionFavItem"))
                Paper.book().delete("positionFavItem")
                Paper.book().delete("symbolForDeleteFavorite")
                loadDataModel()
            }
            dialog.dismiss()

        }
        dialog.negative_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
}
