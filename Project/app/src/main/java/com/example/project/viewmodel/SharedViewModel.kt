package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var blankMessage: String = " "

    var dateDetail: String = ""

    var dateToday: String = ""

    // 投稿した画像のBitmap
    lateinit var imageData: ByteArray

    var bodyWeight: String = blankMessage // 体重
        // private set

    var bodyFatPercentage: String = blankMessage // 体脂肪率

    var skeletalMusclePercentage: String = blankMessage // 骨格筋率

    var basalMetabolicRate: String = blankMessage // 基礎代謝

}