package com.example.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.DBHelper
import com.example.project.PagerAdapter
import com.example.project.PhysicalRecordContract
import com.example.project.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.project.R

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

        val dbHelper = DBHelper(activity!!)

        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE,
            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_CREATED_AT)

        val sql = "select bodyWeight, bodyFatPercentage, createdAt from physicalRecord where createdAt <= '2020-05-02 23:59:59' and createdAt >= '2020-05-02 00:00:00';"
        val cursor = db.rawQuery(sql, null)

        with(cursor) {
            while (moveToNext()) {
                model.bodyWeight = cursor.getString(0)
                model.bodyFatPercentage = cursor.getString(1)
                model.basalMetabolicRate = cursor.getString(2)
            }
        }


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
            valueTextColor = Color.BLACK
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