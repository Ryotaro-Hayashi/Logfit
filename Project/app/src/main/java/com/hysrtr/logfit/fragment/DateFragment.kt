package com.hysrtr.logfit.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
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
import com.hysrtr.logfit.DBHelper
import com.hysrtr.logfit.PhysicalRecordContract
import com.hysrtr.logfit.R
import com.hysrtr.logfit.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_date.*

// Fragment クラスを継承
class DateFragment : Fragment(R.layout.fragment_date) {

    private lateinit var model: SharedViewModel

    private lateinit var dateView: TextView

    private var detailDayData: Array<String?> = arrayOf("", "", "", "") // 詳細表示する日の登録データ
    private var  detailDayImageData: ByteArray = byteArrayOf() // 詳細表示する日に登録した画像データ

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateView = view.findViewById(R.id.dateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        dateView.text = model.calendarDateFormatted

        // データを取得
        getDetailDateData()

        updateView()

    }

    // 値を表示する関数
    private fun updateView() {
        detailDayData[0]?.let { bodyWeightView.changeSizeOfText(it, "  kg", 14) }
        detailDayData[1]?.let { bodyFatPercentageView.changeSizeOfText(it, "  %", 14) }
        detailDayData[2]?.let { skeletalMusclePercentageView.changeSizeOfText(it, "  %", 14) }
        detailDayData[3]?.let { basalMetabolicRateView.changeSizeOfText(it, "  kcal", 14) }

        // 少なくとも1つ以上の要素がある時、画像をセット
        if (detailDayImageData.any()) {
            // bytearrayをbitmapに変換
            val bitmap = BitmapFactory.decodeByteArray(detailDayImageData,0, detailDayImageData.size)
            imageView.setImageBitmap(bitmap)
        }
    }

    // TextViewの一部のスタイルを変更する関数
    private fun TextView.changeSizeOfText(number: String, unit: String, size: Int){

        if (number.isEmpty()) {
            text = "未入力"
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
                number.length, // end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 数値をboldにする
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0, // start
                number.length, // end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // TextViewにSpannableStringBuilderをセット
            text = spannable
        }
    }

    private fun getDetailDateData() {
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
                detailDayData[0] = cursor.getString(0)
                detailDayData[1] = cursor.getString(1)
                detailDayData[2] = cursor.getString(2)
                detailDayData[3] = cursor.getString(3)
                detailDayImageData = cursor.getBlob(4)
            }
        }
    }
}