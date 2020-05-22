package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import java.time.format.DateTimeFormatter

class SharedViewModel : ViewModel() {

    var dateDetail: String = "" // 詳細表示する日付
    var calendarDateFormatted: String = ""

    var todayData: Array<String?> = arrayOf("", "", "", "") // 今日の登録データ
    lateinit var  todayImageData: ByteArray // 今日登録した画像データ

    // ホームでの日付表示用のフォーマット
    val homeDateFormatter = DateTimeFormatter.ofPattern("M月d日")
}