package com.example.project.fragment

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


// Fragment クラスを継承
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var model: SharedViewModel

    private lateinit var calendarView: CalendarView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)

        calendarView = view.findViewById(R.id.calendar)

        // 初期選択日を取得
        val defaultDate = calendarView.date

        // 日付変更イベントを追加
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = "$year/$month/$dayOfMonth"
            Toast.makeText(context, date, Toast.LENGTH_SHORT).show()
        }
    }
}