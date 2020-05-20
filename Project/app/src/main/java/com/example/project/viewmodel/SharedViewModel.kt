package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var dateDetail: String = "" // 詳細表示する日付

    var dateToday: String = "" // 今日の日付

    var dateYesterday: String = "" // 昨日の日付

    var todayData: Array<String?> = arrayOf("", "", "", "") // 今日の登録データ
    lateinit var  todayImageData: ByteArray // 今日登録した画像データ

    var detailDayData: Array<String?> = arrayOfNulls(4) // 詳細表示する日の登録データ
    lateinit var  detailDayImageData: ByteArray // 詳細表示する日に登録した画像データ

}