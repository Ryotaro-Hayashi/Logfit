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

    // 体脂肪を表示するHomeのTextViewを初期化
    private lateinit var bodyWeight: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 体脂肪を表示するHomeのTextView
        bodyWeight = view.findViewById(R.id.bodyWeight)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // viewmodelの値をtextViewに格納
        updateView()
    }

    private fun updateView() {
        bodyWeight.text = model.bodyWeight.toString()
    }
}