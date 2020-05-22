package com.example.project.fragment

import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project.DBHelper
import com.example.project.PhysicalRecordContract
import com.example.project.viewmodel.SharedViewModel
import com.example.project.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
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

        calendarView = view.findViewById(R.id.calendar)

        val calendar = Calendar.getInstance()

        // カレンダーの選択可能日を設定
        calendarView.maxDate = calendar.timeInMillis

        // フォーマット
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        // 詳細表示ページでの日付表示用のフォーマット
        val calendarDateFormatter = SimpleDateFormat("Y年M月d日", Locale.US)

        // 初期選択日を取得
        val defaultDate = calendarView.date
        model.dateDetail = formatter.format(defaultDate)
        model.calendarDateFormatted = calendarDateFormatter.format(defaultDate)

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

    // SQL文で日付を指定するためにフォーマットを修正する関数
    private fun arrangeFormatt(x: Int): String {
        if (x >= 10) {
            return x.toString()
        } else {
            return "0$x"
        }
    }
}