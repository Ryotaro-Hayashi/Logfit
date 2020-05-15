package com.example.project.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_date.*

// Fragment クラスを継承
class DateFragment : Fragment(R.layout.fragment_date) {

    private lateinit var model: SharedViewModel

    private lateinit var dateView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateView = view.findViewById(R.id.dateView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        dateView.text = model.dateDetail
        bodyWeightView.text = model.bodyWeight.toString() + " kg"
        bodyFatPercentageView.text = model.bodyFatPercentage.toString() + " %"
        skeletalMusclePercentageView.text = model.skeletalMusclePercentage.toString() + " ％"
        basalMetabolicRateView.text = model.basalMetabolicRate.toString() + " kcal"
        val bitmap = BitmapFactory.decodeByteArray(model.imageData,0,model.imageData.size)
        imageView.setImageBitmap(bitmap)
    }
}