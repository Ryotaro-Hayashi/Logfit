package com.example.project.fragment

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project.viewmodel.SharedViewModel
import com.example.project.R
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

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        calendarView = view.findViewById(R.id.calendar)

        val c = Calendar.getInstance()

        // 初期選択日を取得
        val defaultDate = calendarView.date
        model.dateDetail = format.format(defaultDate)

        // 日付変更イベントを追加
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 月は0から数える仕様になっているので1をたす
            val year = year.toString()
            var month = arrangeFormatt(month + 1)
            var dayOfMonth = arrangeFormatt(dayOfMonth)
            val date = "$year-$month-$dayOfMonth"
            model.dateDetail = date

            // 画面遷移
            val action = CalendarFragmentDirections.actionNavigationCalendarToNavigationDate()
            findNavController().navigate(action)
        }
    }

    private fun arrangeFormatt(x: Int): String {
        if (x >= 10) {
            return x.toString()
        } else {
            return "0$x"
        }
    }
}