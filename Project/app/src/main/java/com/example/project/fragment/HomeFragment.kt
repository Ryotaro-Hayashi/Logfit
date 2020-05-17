package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.DBHelper
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
import kotlinx.android.synthetic.main.fragment_date.*


// Fragment クラスを継承
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var model: SharedViewModel

    // グラフのデータラベルのスタイルとフォントファミリーの設定
    private var mTypeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // グラフの描画
        lineChart.data = lineData()

        // グラフ細かい設定
        setupLineChart()

        // adapter で表示するフラグメントを設定
        val fragmentList = arrayListOf<Fragment>(
            TodayFragment(), YesterdayFragment()
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

        viewPager.setRotationY(180F);

    }

    // データ作成
    private fun lineData():LineData {

        val values = mutableListOf<Entry>()

        // データベースのクラスをインスタンス化
        val dbHelper = DBHelper(activity!!)

        // 読み込み専用で接続
        val db = dbHelper.readableDatabase

        // 今日の始まり
        val dateBegin = model.dateToday + " 00:00:00"
        // 今日の終わり
        val dateEnd = model.dateToday + " 23:59:59"

        // 今日のデータを取得するSQL文
        val sql = "select bodyWeight from physicalRecord where createdAt <= ? and createdAt >= ?  order by _id desc limit 1;"
        // データを取得
        val cursor = db.rawQuery(sql, arrayOf(dateEnd, dateBegin))

        val dataArray: Array<Float?> = arrayOfNulls(6)


        with(cursor) {
            while (moveToNext()) {
                dataArray[0] = cursor.getFloat(0)
            }
        }

        dataArray[0]?.let { Entry(1F, it) }?.let { values.add(it) }
        values.add(Entry(2.toFloat(), 57.toFloat()))
        values.add(Entry(3.toFloat(), 56.toFloat()))
        values.add(Entry(4.toFloat(), 58.toFloat()))
        values.add(Entry(5.toFloat(), 57.toFloat()))
        values.add(Entry(6.toFloat(), 59.toFloat()))
        values.add(Entry(7.toFloat(), 59.toFloat()))

        // グラフの描画設定
        val yVals = LineDataSet(values, "体重").apply {
            // 線の色
            color = Color.parseColor("#FFA500")
            // 点の円を描画
            setDrawCircles(true)
            // 点の円の中の穴を描画
            setDrawCircleHole(true)
            // 点の円の色を設定
            setCircleColor(Color.parseColor("#FF8C00"))
            // 点の値非表示
            setDrawValues(false)
            // テキストの色を設定
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
            // タッチすると出るグリッドを非表示表示
//             setTouchEnabled()

            // データラベルの表示
            legend.apply {
                form = Legend.LegendForm.LINE
                // スタイルとフォントの設定
                typeface = mTypeface
                textSize = 15f
                textColor = Color.WHITE
                // 位置設定
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }

            // y軸左側
            axisLeft.apply {
                textColor = Color.WHITE
            }

            // y軸右側
            axisRight.apply {
                // 軸やラベルを無効化
                isEnabled = false
            }

            // X軸の設定
            xAxis.apply {
                isEnabled = true
                textColor = Color.WHITE
                // x軸のグリッドを無効化
                setDrawGridLines(false)
            }

            // アニメーション
//            lineChart.animateXY(1500,1000)
        }

        // y軸
        val yLeft: YAxis = lineChart.axisLeft
        // y軸を描画しない
        yLeft.setDrawAxisLine(false);

        // x軸
        val xBottom: XAxis = lineChart.xAxis
        // x軸を描画しない
        xBottom.setDrawAxisLine(false);

        // x軸のラベル
        val labels = arrayOf(
            "", "", "3日", "4日"
            , "5日", "6日", "7日", "7日"
        )

        // 描画したグラフのx軸を取得
        val xAxis = lineChart.xAxis
        // x軸のラベルを設定
        xAxis.setValueFormatter(IndexAxisValueFormatter(labels))
        // x軸のラベルの位置設定
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)

    }

}