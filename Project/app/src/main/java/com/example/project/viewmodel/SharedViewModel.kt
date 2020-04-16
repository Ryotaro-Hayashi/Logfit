package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var bodyWeight: String = "入力されていません" // 体重
        // private set

    var bodyFatPercentage: String = "入力されていません" // 体脂肪率

    var skeletalMusclePercentage: String = "入力されていません" // 骨格筋率

    var basalMetabolicRate: String = "入力されていません" // 基礎代謝

}