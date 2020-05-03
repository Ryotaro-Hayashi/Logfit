package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.PagerAdapter
import com.example.project.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.project.R
import com.github.mikephil.charting.components.XAxis


// Fragment クラスを継承
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var model: SharedViewModel

    // スタイルとフォントファミリーの設定
    private var mTypeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // グラフの設定
        setupLineChart()

        // データの設定
        // 引数にデータ数と縦軸のメモリを指定
        lineChart.data = lineData()

        val fragmentList = arrayListOf<Fragment>(
            TodayFragment(), RecordFragment()
        )

        /// adapterのインスタンス生成
        val adapter = fragmentManager?.let {
            PagerAdapter(
                it,
                fragmentList
            )
        }

        /// adapterをセット
        viewPager.adapter = adapter

    }

    // データ作成
    private fun lineData():LineData {

        val values = mutableListOf<Entry>()

        values.add(Entry(1.toFloat(), 56.toFloat()))
        values.add(Entry(2.toFloat(), 57.toFloat()))
        values.add(Entry(3.toFloat(), 56.toFloat()))
        values.add(Entry(4.toFloat(), 58.toFloat()))
        values.add(Entry(5.toFloat(), 57.toFloat()))
        values.add(Entry(6.toFloat(), 59.toFloat()))
        values.add(Entry(7.toFloat(), 59.toFloat()))

        // グラフのレイアウトの設定
        val yVals = LineDataSet(values, "体重").apply {
            // 線の色
            color = Color.parseColor("#FFA500")
            setDrawCircles(true)
            setDrawCircleHole(true)
            setCircleColor(Color.parseColor("#FF8C00"))
            // 点の値非表示
            setDrawValues(false)
            valueTextColor = Color.WHITE
            // テキストサイズ
            valueTextSize = 16f
            // 線の太さ
            lineWidth = 3f
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
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            axisLeft.apply {
                isEnabled = true
                textColor = Color.WHITE
                setDrawLabels(true)

//                // 横線
//                setDrawGridLines(false)
//                setDrawAxisLine(true)
                // ラベルを非表示
//                setDrawLabels(false)
//                setDrawZeroLine(true)
            }

            axisRight.apply {
                isEnabled = false
                setEnabled(false)
                setDrawGridLines(false)
            }

            // X軸の設定
            xAxis.apply {
                isEnabled = true
                textColor = Color.WHITE
                setDrawGridLines(false)
//                setDrawAxisLine(true)
            }
//            lineChart.animateXY(1500,1000)
        }

        val yLeft: YAxis = lineChart.axisLeft
        yLeft.setDrawAxisLine(false);

        val xBottom: XAxis = lineChart.xAxis
        xBottom.setDrawAxisLine(false);

        val labels = arrayOf(
            "", "", "3日", "4日"
            , "5日", "6日", "7日", "7日"
        )

        val xAxis = lineChart.xAxis
        xAxis.setValueFormatter(IndexAxisValueFormatter(labels))
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)

    }

}