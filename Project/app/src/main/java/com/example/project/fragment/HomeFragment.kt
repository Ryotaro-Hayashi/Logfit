package com.example.project.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel

// Fragment クラスを継承
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var model: SharedViewModel

    // Homeで表示するTextViewを初期化
    private lateinit var bodyWeightView: TextView
    private lateinit var bodyFatPercentageView: TextView
    private lateinit var skeletalMusclePercentageView: TextView
    private lateinit var basalMetabolicRateView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Homeで表示するTextViewを取得
        bodyWeightView = view.findViewById(R.id.bodyWeightView)
        bodyFatPercentageView = view.findViewById(R.id.bodyFatPercentageView)
        skeletalMusclePercentageView = view.findViewById(R.id.skeletalMusclePercentageView)
        basalMetabolicRateView = view.findViewById(R.id.basalMetabolicRateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // viewmodelの値をtextViewに格納
        updateView()
    }

    private fun updateView() {
        bodyWeightView.text = model.bodyWeight.toString()
        bodyFatPercentageView.text = model.bodyFatPercentage.toString()
        skeletalMusclePercentageView.text = model.skeletalMusclePercentage.toString()
        basalMetabolicRateView.text = model.basalMetabolicRate.toString()
    }
}