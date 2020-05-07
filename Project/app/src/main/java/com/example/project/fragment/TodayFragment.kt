package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.provider.BaseColumns
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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

        val sql = "select bodyWeight, bodyFatPercentage, createdAt from physicalRecord where createdAt <= ? and createdAt >= ?  order by _id desc limit 1;"
        val cursor = db.rawQuery(sql, arrayOf(dateBegin, dateEnd))

        with(cursor) {
            while (moveToNext()) {
                model.bodyWeight = cursor.getString(0)
                model.bodyFatPercentage = cursor.getString(1)
//                model.basalMetabolicRate = cursor.getString(2)
            }
        }
    }

    private fun updateView() {
        bodyWeightView.changeSizeOfText(model.bodyWeight.toString(), "  %", 14)
        bodyFatPercentageView.changeSizeOfText(model.bodyFatPercentage.toString(), "  %", 14)

        skeletalMusclePercentageView.changeSizeOfText(model.skeletalMusclePercentage.toString(), "  %", 14)
        basalMetabolicRateView.changeSizeOfText(model.basalMetabolicRate, "  kcal", 14)
    }

    fun TextView.changeSizeOfText(number: String, unit: String, size: Int){

        if (number.isEmpty()) {
            val message = "未入力"

            text = message
            textSize = 14F

        } else {
            // 対象となる文字列を引数に渡し、SpannableStringBuilderのインスタンスを生成
            val spannable = SpannableStringBuilder(number + unit)

            // 単位のサイズを変更
            spannable.setSpan(
                AbsoluteSizeSpan(size, true),
                number.length, // start
                spannable.length, // end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 数値の色をに変更
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF8C00")),
                0, // start
                unit.length, // end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 数値をboldにする
            spannable.setSpan(
                StyleSpan(BOLD),
                0, // start
                number.length, // end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // TextViewにSpannableStringBuilderをセット
            text = spannable
        }
    }

}