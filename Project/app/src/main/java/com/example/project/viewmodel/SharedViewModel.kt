package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var bodyWeight: String = "" // 体重
        // private set

    var bodyFatPercentage: String = "" // 体脂肪率

    var skeletalMusclePercentage: String = "" // 骨格筋率

    var basalMetabolicRate: String = "" // 基礎代謝

}