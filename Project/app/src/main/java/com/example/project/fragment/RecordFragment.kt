package com.example.project.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.viewmodel.SharedViewModel

// Fragment クラスを継承
class RecordFragment : Fragment(R.layout.fragment_record) {

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

        // 登録ボタン
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // 登録ボタンからHomeへの画面遷移
            val action = RecordFragmentDirections.actionNavigationRecordToNavigationHome()
            findNavController().navigate(action)

            // フォームに入力した値を反映
            model.updateInformation()
            // Home のTextViewに反映
            register()
        }
    }

    // 入力した情報をHomeで表示するTextViewに格納
    private fun register() {
        bodyWeight.text = model.bodyWeight.toString()
    }
}