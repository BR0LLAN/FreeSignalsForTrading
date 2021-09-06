package com.devcraft.freesignalsfortrading.presentation.ui.quotes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.app.Constants
import com.devcraft.freesignalsfortrading.databinding.FragmentQuotesBinding
import com.devcraft.freesignalsfortrading.presentation.ui.alert.NotificationSetting
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_quotes.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuotesFragment : Fragment(){

    private val quotesViewModel by sharedViewModel<QuotesViewModel>()
    private var _binding: FragmentQuotesBinding? = null
    private var quotesAdapter: QuotesAdapter = QuotesAdapter()
    private lateinit var preferences:SharedPreferences
    private lateinit var preferencesForFavorite:SharedPreferences

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        initObservers()
        preferences =  requireContext().getSharedPreferences("Q", Context.MODE_PRIVATE)
        preferencesForFavorite =  requireContext().getSharedPreferences("favorite", Context.MODE_PRIVATE)

        _binding = FragmentQuotesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_quotes.adapter = quotesAdapter
        rv_quotes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quotes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        quotesViewModel.loadData(Constants.q)
    }

    private fun initObservers() {
        quotesViewModel.quotesCurrencies.observe(viewLifecycleOwner, {
            quotesAdapter.items = it
        })
        quotesViewModel.getQuotesFailure.observe(this, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.notification -> {
                if(Paper.book().contains("symbol")){
                    startActivity(Intent(requireContext(), NotificationSetting::class.java))
                }
                else{
                    Toast.makeText(requireContext(), "You must choose an item  from  the list for adding a alert price", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.favorite -> {
                if(Paper.book().contains("symbol")){
                    preferencesForFavorite.edit().putString(Paper.book().read("symbol"), Paper.book().read("symbol")).apply()
                    Paper.book().delete("symbol")
                    Toast.makeText(requireContext(), "Added  to favorites ", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), "You must choose an item  from  the list for adding a fav", Toast.LENGTH_SHORT).show()
                }
            }

            else -> ""
        }
        return super.onOptionsItemSelected(item)
        
    }


}