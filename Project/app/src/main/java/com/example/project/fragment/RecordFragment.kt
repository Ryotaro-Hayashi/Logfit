package com.example.project.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project.viewmodel.SharedViewModel
import com.example.project.R
import java.io.IOException

// Fragment クラスを継承
class RecordFragment : Fragment(R.layout.fragment_record) {

    private lateinit var model: SharedViewModel

    // 入力フォームを初期化
    private lateinit var bodyWeightForm: EditText // 体重入力フォーム
    private lateinit var bodyFatPercentageForm: EditText // 体脂肪入力フォーム
    private lateinit var skeletalMusclePercentageForm: EditText // 骨格筋率入力フォーム
    private lateinit var basalMetabolicRateForm: EditText // 基礎代謝入力フォーム

    private val READ_REQUEST_CODE = 42

    private lateinit var contentResolver: ContentResolver
    private lateinit var imageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 入力フォームを取得
        bodyWeightForm = view.findViewById(R.id.bodyWeightForm)
        bodyFatPercentageForm = view.findViewById(R.id.bodyFatPercentageFrom)
        skeletalMusclePercentageForm = view.findViewById(R.id.skeletalMusclePercentageForm)
        basalMetabolicRateForm = view.findViewById(R.id.basalMetabolicRateForm)

        imageView = view.findViewById(R.id.imageView)

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val imagePicker = view.findViewById<Button>(R.id.imagePicker)
        imagePicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        // 登録ボタン
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // 登録ボタンからHomeへの画面遷移
            val action = RecordFragmentDirections.actionNavigationRecordToNavigationHome()
            findNavController().navigate(action)

            // フォームに入力した値を反映
            if (bodyWeightForm.text.isBlank()) { // 体重
                model.bodyWeight = model.blankMessage
            } else {
                model.bodyWeight = bodyWeightForm.text.toString()
            }

            // フォームに入力した値を反映
            if (bodyFatPercentageForm.text.isBlank()) { // 体重
                model.bodyFatPercentage = model.blankMessage
            } else {
                model.bodyFatPercentage = bodyWeightForm.text.toString()
            }

            // フォームに入力した値を反映
            if (skeletalMusclePercentageForm.text.isBlank()) { // 体重
                model.skeletalMusclePercentage = model.blankMessage
            } else {
                model.skeletalMusclePercentage = skeletalMusclePercentageForm.text.toString()
            }

            // フォームに入力した値を反映
            if (basalMetabolicRateForm.text.isBlank()) { // 体重
                model.basalMetabolicRate = model.blankMessage
            } else {
                model.basalMetabolicRate = basalMetabolicRateForm.text.toString()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (resultData != null) {
                uri = resultData.data
                try {
                    val source = uri?.let { ImageDecoder.createSource(contentResolver, it) }
                    val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                    imageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}