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

        // 初期選択日を取得
        val defaultDate = calendarView.date
        model.dateDetail = formatter.format(defaultDate)

        // 日付変更イベントを追加
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 月は0から数える仕様になっているので1をたす
            val year = year.toString()
            var month = arrangeFormatt(month + 1)
            var dayOfMonth = arrangeFormatt(dayOfMonth)
            val date = "$year-$month-$dayOfMonth"
            model.dateDetail = date

            val dbHelper = DBHelper(activity!!)

            val db = dbHelper.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT,
                PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE,
                PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_CREATED_AT)

            val dateBegin = model.dateDetail + " 00:00:00"
            val dateEnd = model.dateDetail + " 23:59:59"

            val sql = "select bodyWeight, bodyFatPercentage, skeletalMusclePercentage, basalMetabolicRate, bitmap from physicalRecord where createdAt <= ? and createdAt >= ?  order by _id desc limit 1;"
            val cursor = db.rawQuery(sql, arrayOf(dateEnd, dateBegin))

            with(cursor) {
                while (moveToNext()) {
                    model.bodyWeight = cursor.getString(0)
                    model.bodyFatPercentage = cursor.getString(1)
                    model.skeletalMusclePercentage = cursor.getString(2)
                    model.basalMetabolicRate = cursor.getString(3)
                    model.imageData = cursor.getBlob(4)
                }
            }

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