package com.example.project.fragment

import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.DBHelper
import com.example.project.PhysicalRecordContract
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment(R.layout.fragment_today) {

    private lateinit var model: SharedViewModel

    // Homeで表示するTextViewを初期化
    private lateinit var bodyWeightView: TextView
    private lateinit var bodyFatPercentageView: TextView
    private lateinit var skeletalMusclePercentageView: TextView
    private lateinit var basalMetabolicRateView: TextView
    private lateinit var dateView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Homeで表示するTextViewを取得
        bodyWeightView = view.findViewById(R.id.bodyWeightView)
        bodyFatPercentageView = view.findViewById(R.id.bodyFatPercentageView)
        skeletalMusclePercentageView = view.findViewById(R.id.skeletalMusclePercentageView)
        basalMetabolicRateView = view.findViewById(R.id.basalMetabolicRateView)

        dateView = view.findViewById(R.id.dateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)

        model.dateToday = formatted

        dateView.text = formatted

        // viewmodelの値をtextViewに格納
        updateView()

        val dbHelper = DBHelper(activity!!)

        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_CREATED_AT)

        val dateBegin = model.dateToday + " 23:59:59"
        val dateEnd = model.dateToday + " 00:00:00"

        val sql = "select bodyWeight, bodyFatPercentage, createdAt from physicalRecord where createdAt <= ? and createdAt >= ?  limit 1;"
        val cursor = db.rawQuery(sql, arrayOf(dateBegin, dateEnd))

        with(cursor) {
            while (moveToNext()) {
                model.bodyWeight = cursor.getString(0)
                model.bodyFatPercentage = cursor.getString(1)
                model.basalMetabolicRate = cursor.getString(2)
            }
        }
    }

    private fun updateView() {
        bodyWeightView.text = model.bodyWeight.toString() + " kg"
        bodyFatPercentageView.text = model.bodyFatPercentage.toString() + " %"
        skeletalMusclePercentageView.text = model.skeletalMusclePercentage.toString() + " ％"
        basalMetabolicRateView.text = model.basalMetabolicRate.toString() + " kcal"
    }
}