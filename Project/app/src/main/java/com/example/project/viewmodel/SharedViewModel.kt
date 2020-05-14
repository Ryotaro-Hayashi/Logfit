package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var blankMessage: String = "" // 空のメッセージ

    var dateDetail: String = "" // 詳細表示する日付

    var dateToday: String = "" // 今日の日付

    lateinit var imageData: ByteArray // 画像

    var bodyWeight: String = blankMessage // 体重

    var bodyFatPercentage: String = blankMessage // 体脂肪率

    var skeletalMusclePercentage: String = blankMessage // 骨格筋率

    var basalMetabolicRate: String = blankMessage // 基礎代謝

}