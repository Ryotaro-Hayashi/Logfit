package com.example.project.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_date.*

// Fragment クラスを継承
class DateFragment : Fragment(R.layout.fragment_date) {

    private lateinit var model: SharedViewModel

    private lateinit var dateView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateView = view.findViewById(R.id.dateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        dateView.text = model.dateDetail

        updateView()

        // 画像を表示
//        val bitmap = BitmapFactory.decodeByteArray(model.detailDayImageData,0,model.detailDayImageData.size)
//        imageView.setImageBitmap(bitmap)
    }

    // 値を表示する関数
    private fun updateView() {
        model.detailDayData[0]?.let { bodyWeightView.changeSizeOfText(it, "  kg", 14) }
        model.detailDayData[1]?.let { bodyFatPercentageView.changeSizeOfText(it, "  %", 14) }
        model.detailDayData[2]?.let { skeletalMusclePercentageView.changeSizeOfText(it, "  %", 14) }
        model.detailDayData[3]?.let { basalMetabolicRateView.changeSizeOfText(it, "  kcal", 14) }
    }

    // TextViewの一部のスタイルを変更する関数
    fun TextView.changeSizeOfText(number: String, unit: String, size: Int){

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
                unit.length, // end
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
}