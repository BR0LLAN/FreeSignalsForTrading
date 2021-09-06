package com.devcraft.freesignalsfortrading.presentation.ui.alert

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.devcraft.freesignalsfortrading.R
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_notification_setting.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationSetting : AppCompatActivity(R.layout.activity_notification_setting) {

    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesForNotify: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        Paper.init(applicationContext)
        preferencesForNotify =
            applicationContext.getSharedPreferences("ForNotify", Context.MODE_PRIVATE)
        initListeners()
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        quote_symbol_selected.text = Paper.book().read<String>("symbol")
        quote_value_selected.text = Paper.book().read<Double>("quoteValue").toString()
    }

    private fun initListeners() {
        btn_add_quotes.setOnClickListener {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            val inputCustomPrice: String = input_custom_price.text.toString()
            val inputCustomPauseNotification: String = input_custom_pause_notification.text.toString()
            val inputCustomContentTextNotify: String = input_custom_content_text_notify.text.toString()


            val strData = "$inputCustomPrice;$inputCustomPauseNotification;$inputCustomContentTextNotify;$currentDate"
            println(strData)
            preferencesForNotify
                .edit()
                .putString(
                    Paper.book().read("symbol"), strData)
                .apply()



            Toast.makeText(applicationContext, "Alert price was added", Toast.LENGTH_SHORT).show()
            finish()
        }
        btn_back_to.setOnClickListener {
            Paper.book().delete("symbol")
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}