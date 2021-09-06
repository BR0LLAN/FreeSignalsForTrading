package com.devcraft.freesignalsfortrading.presentation.ui.alert

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.app.Constants
import com.devcraft.freesignalsfortrading.databinding.FragmentAlertsBinding
import com.devcraft.freesignalsfortrading.databinding.FragmentAlertsBinding.*
import com.devcraft.freesignalsfortrading.entities.QuotesCurrency
import com.devcraft.freesignalsfortrading.presentation.ui.quotes.QuotesViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_alerts.*
import kotlinx.android.synthetic.main.pop_up_confirm.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AlertFragment : Fragment() {

    private val quotesViewModel by sharedViewModel<QuotesViewModel>()
    private var _binding: FragmentAlertsBinding? = null
    private var alertForView: MutableList<QuotesCurrency> = mutableListOf()
    private lateinit var preferences: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        preferences = requireContext().getSharedPreferences("ForNotify", Context.MODE_PRIVATE)
        dialog = Dialog(requireContext())
        _binding = inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_alert.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //quotesViewModel.loadData()
    }

    override fun onResume() {
        super.onResume()
        setAdapterAlerts()
        quotesViewModel.loadData(Constants.q)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.alert, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_alert -> {
                showDialogConfirm()
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapterAlerts() {
        for (value in quotesViewModel.quotesCurrencies.value!!) {
            if (preferences.contains(value.symbol)) {
                alertForView.add(value)

            }
        }
        rv_alert.adapter = AlertAdapter(alertForView)
    }

    private fun showDialogConfirm() {
        dialog.setContentView(R.layout.pop_up_confirm)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialog.positive_btn.setOnClickListener {
            if (Paper.book().contains("symbolForDelete")) {
                preferences.edit().remove(Paper.book().read("symbolForDelete")).apply()
                alertForView.removeAt(Paper.book().read("position"))
                rv_alert.removeViewAt(Paper.book().read("position"))
                Paper.book().delete("position")
                Paper.book().delete("symbolForDelete")
                dialog.dismiss()
            }
        }
        dialog.negative_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Paper.book().contains("symbol")) {
            Paper.book().delete("symbol")
        }

    }
}