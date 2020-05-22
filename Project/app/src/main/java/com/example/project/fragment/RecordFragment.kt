package com.example.project.fragment

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.BaseColumns
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.project.DBHelper
import com.example.project.PhysicalRecordContract
import com.example.project.viewmodel.SharedViewModel
import com.example.project.R
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import android.util.Log
import kotlinx.android.synthetic.main.fragment_date.*


// Fragment クラスを継承
class RecordFragment : Fragment(R.layout.fragment_record) {

    private lateinit var model: SharedViewModel

    // 入力フォームを初期化
    private lateinit var bodyWeightForm: EditText // 体重入力フォーム
    private lateinit var bodyFatPercentageForm: EditText // 体脂肪入力フォーム
    private lateinit var skeletalMusclePercentageForm: EditText // 骨格筋率入力フォーム
    private lateinit var basalMetabolicRateForm: EditText // 基礎代謝入力フォーム

    private lateinit var imageView: ImageView

    // テーブルのidを初期化
    private var physicalRecordId = 0L

    // 定数
    companion object {
        private const val CHOOSE_PHOTO: Int = 110
    }

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
            // ピッカーを使用してファイルを選択するためのIntent
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            // 開くことができるファイルのカテゴリーを選択
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 取得するファイルの形式をフィルター
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "写真を選択"), CHOOSE_PHOTO)
        }

        bodyWeightForm.setText(model.todayData[0])
        bodyFatPercentageForm.setText(model.todayData[1])
        skeletalMusclePercentageForm.setText(model.todayData[2])
        basalMetabolicRateForm.setText(model.todayData[3])

        // 少なくとも1つ以上の要素がある時、画像をセット
        if (model.todayImageData.any()) {
            // bytearrayをbitmapに変換
            val bitmap = BitmapFactory.decodeByteArray(model.todayImageData,0,model.todayImageData.size)
            imageView.setImageBitmap(bitmap)
        }

        // 登録ボタン
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            // 登録ボタンからHomeへの画面遷移を設定
            val action = RecordFragmentDirections.actionNavigationRecordToNavigationHome()
            // 画面遷移
            findNavController().navigate(action)

            // データベースにアクセス
            val dbHelper = DBHelper(activity!!)

            // 書き込みモードでデータにアクセス
            val db = dbHelper.writableDatabase

            // 格納するデータ
            val values = ContentValues().apply {
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT, bodyWeightForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE, bodyFatPercentageForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_SKELETAL_MUSCLE_PERCENTAGE, skeletalMusclePercentageForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BASAL_METABOLIC_RATE, basalMetabolicRateForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BITMAP, model.todayImageData)
            }

            // テーブルに書き込み
            physicalRecordId = db.insert(PhysicalRecordContract.PhysicalRecordEntry.TABLE_NAME, null, values)
            db.close()
        }
    }

    // ピッカーから画像を選択すると、onActivityResult() が呼び出される
    // 選択した画像を指す URI が resultData パラメータに含まれ返ってくる
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            val bitmap = getBitmapFromUri(data.data)
            bitmap?:return

            // 画像をセット
            imageView.setImageBitmap(bitmap)

            model.todayImageData = getBinaryFromBitmap(bitmap)
        }
    }

    // bitmapを取得
    private fun getBitmapFromUri(uri: Uri?): Bitmap? {
        uri?:return null

        val parcelFileDescriptor: ParcelFileDescriptor? =
            activity?.contentResolver?.openFileDescriptor(uri, "r")
        parcelFileDescriptor?:return null

        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    // Binaryを取得
    private fun getBinaryFromBitmap(bitmap:Bitmap):ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}
