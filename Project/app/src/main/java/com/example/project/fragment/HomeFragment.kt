package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.project.R



// Fragment クラスを継承
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var model: SharedViewModel

    // Homeで表示するTextViewを初期化
    private lateinit var bodyWeightView: TextView
    private lateinit var bodyFatPercentageView: TextView
    private lateinit var skeletalMusclePercentageView: TextView
    private lateinit var basalMetabolicRateView: TextView

    // スタイルとフォントファミリーの設定
    private var mTypeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Homeで表示するTextViewを取得
        bodyWeightView = view.findViewById(R.id.bodyWeightView)
        bodyFatPercentageView = view.findViewById(R.id.bodyFatPercentageView)
        skeletalMusclePercentageView = view.findViewById(R.id.skeletalMusclePercentageView)
        basalMetabolicRateView = view.findViewById(R.id.basalMetabolicRateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // viewmodelの値をtextViewに格納
        updateView()

        // グラフの設定
        setupLineChart()

        // データの設定
        // 引数にデータ数と縦軸のメモリを指定
        lineChart.data = lineData()
    }

    private fun updateView() {
        bodyWeightView.text = model.bodyWeight.toString() + " kg"
        bodyFatPercentageView.text = model.bodyFatPercentage.toString() + " %"
        skeletalMusclePercentageView.text = model.skeletalMusclePercentage.toString() + " ％"
        basalMetabolicRateView.text = model.basalMetabolicRate.toString() + " kcal"
    }

    // データ作成
    private fun lineData():LineData {

        val values = mutableListOf<Entry>()

        values.add(Entry(1.toFloat(), 0.toFloat()))
        values.add(Entry(2.toFloat(), 15.toFloat()))
        values.add(Entry(3.toFloat(), 9.toFloat()))
        values.add(Entry(4.toFloat(), 90.toFloat()))
        values.add(Entry(5.toFloat(), 9.toFloat()))
        values.add(Entry(6.toFloat(), 90.toFloat()))

        // グラフのレイアウトの設定
        val yVals = LineDataSet(values, "体重").apply {
            // 線の色
            color = Color.WHITE
            setDrawCircles(true)
            setDrawCircleHole(true)
            // 点の値非表示
            setDrawValues(true)
            valueTextColor = Color.DKGRAY
            // テキストサイズ
            valueTextSize = 12f
            // 線の太さ
            lineWidth = 2f
        }
        val data = LineData(yVals)
        return data
    }

    // グラフの設定をする関数
    private fun setupLineChart(){
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(false)
            isDragEnabled = true
            // 拡大縮小可能
            isScaleXEnabled = true
            setPinchZoom(false)
            // グラフの描画領域の背景指定
            setDrawGridBackground(false)

            //データラベルの表示
            legend.apply {
                form = Legend.LegendForm.LINE
                typeface = mTypeface
                textSize = 15f
                textColor = Color.WHITE
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            axisLeft.apply {
                isEnabled = false
                // 横線
                setDrawGridLines(false)
            }

            axisRight.apply {
                isEnabled = false
                setDrawGridLines(false)
            }

            // X軸の設定
            xAxis.apply {
                isEnabled = false
            }
        }
    }
}