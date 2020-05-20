package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        var dateArrayBeforeFormatted: Array<LocalDateTime?> = arrayOfNulls(7)
        // 日付データ
        var dateFormatted: Array<String?> = arrayOfNulls(7)
        // フォーマットを指定
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 登録データ
        var dataArray: Array<Float?> = arrayOfNulls(7) // 取得したデータを格納する配列

        for (i in dataArray.indices) {
            if (i == 0) {
                // 今日の日付を格納
                dateArrayBeforeFormatted[i] = LocalDateTime.now()
            } else {
                // index の分をマイナスした日付を格納
                dateArrayBeforeFormatted[i] = LocalDateTime.now().minusDays(i.toLong())
            }
            // フォーマットを変更
            dateFormatted[i] = dateArrayBeforeFormatted[i]?.format(formatter)

            val dateBegin = dateFormatted[i].toString() + " 00:00:00"
            val dateEnd = dateFormatted[i].toString() + " 23:59:59"

            // データを取得するSQL文
            val sql = "select bodyWeight from physicalRecord where createdAt <= ? and createdAt >= ?  order by _id desc limit 1;"
            // データを取得
            val cursor = db.rawQuery(sql, arrayOf(dateEnd, dateBegin))

            with(cursor) {
                while (moveToNext()) {
                    dataArray[i] = cursor.getFloat(0)
                }
            }
        }

        dataArray.reverse() // 配列を降順に変更

        for (i in dataArray.indices) { // グラフにデータをプロット
            dataArray[i]?.let { Entry(i.toFloat(), it) }?.let { values.add(it) }
        }

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
        xBottom.setDrawAxisLine(false)

        var dateArrayBeforeFormatted: Array<LocalDateTime?> = arrayOfNulls(7)
        // x軸のラベル
        var dateFormatted: Array<String?> = arrayOfNulls(7)
        // フォーマットを指定
        val formatter = DateTimeFormatter.ofPattern("M/d")

        for (i in dateFormatted.indices) {
            if (i == 0) {
                dateArrayBeforeFormatted[i] = LocalDateTime.now()
            } else {
                dateArrayBeforeFormatted[i] = LocalDateTime.now().minusDays(i.toLong())
            }
            dateFormatted[i] = dateArrayBeforeFormatted[i]?.format(formatter)
        }

        dateFormatted.reverse() // 配列を降順に変更

        // 描画したグラフのx軸を取得
        val xAxis = lineChart.xAxis
        // x軸のラベルを設定
        xAxis.valueFormatter = IndexAxisValueFormatter(dateFormatted)
        // x軸のラベルの位置設定
        xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

}