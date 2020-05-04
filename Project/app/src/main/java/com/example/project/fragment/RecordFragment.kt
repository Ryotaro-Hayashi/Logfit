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

        // 登録ボタン
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            // 登録ボタンからHomeへの画面遷移
            val action = RecordFragmentDirections.actionNavigationRecordToNavigationHome()
            findNavController().navigate(action)

//            // フォームに入力した値を反映
//            if (bodyWeightForm.text.isBlank()) { // 体重
//                model.bodyWeight = model.blankMessage
//            } else {
//                model.bodyWeight = bodyWeightForm.text.toString()
//            }
//
//            // フォームに入力した値を反映
//            if (bodyFatPercentageForm.text.isBlank()) { // 体重
//                model.bodyFatPercentage = model.blankMessage
//            } else {
//                model.bodyFatPercentage = bodyFatPercentageForm.text.toString()
//            }
//
//            // フォームに入力した値を反映
//            if (skeletalMusclePercentageForm.text.isBlank()) { // 体重
//                model.skeletalMusclePercentage = model.blankMessage
//            } else {
//                model.skeletalMusclePercentage = skeletalMusclePercentageForm.text.toString()
//            }
//
//            // フォームに入力した値を反映
//            if (basalMetabolicRateForm.text.isBlank()) { // 体重
//                model.basalMetabolicRate = model.blankMessage
//            } else {
//                model.basalMetabolicRate = basalMetabolicRateForm.text.toString()
//            }

            // データベースにアクセス
            val dbHelper = DBHelper(activity!!)

            // 書き込みモードでデータにアクセス
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT, bodyWeightForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE, bodyFatPercentageForm.text.toString())
                put(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BITMAP, model.imageData)
            }

            // テーブルに書き込み
            physicalRecordId = db.insert(PhysicalRecordContract.PhysicalRecordEntry.TABLE_NAME, null, values)
            db.close()
        }

//        val dbHelper = DBHelper(activity!!)
//
//        val db = dbHelper.readableDatabase
//
//        val projection = arrayOf(
//            BaseColumns._ID,
//            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT,
//            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE,
//            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BITMAP,
//            PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_CREATED_AT)
//
//        val selection = "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT} = ?"
//        val selectionArgs = arrayOf("90")
//
//        val sortOrder = "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT} DESC"
//
//        val cursor = db.query(
//            PhysicalRecordContract.PhysicalRecordEntry.TABLE_NAME,   // The table to query
//            projection,             // The array of columns to return (pass null to get all)
//            selection,              // The columns for the WHERE clause
//            selectionArgs,          // The values for the WHERE clause
//            null,                   // don't group the rows
//            null,                   // don't filter by row groups
//            sortOrder               // The sort order
//        )
//
//        with(cursor) {
//            while (moveToNext()) {
//                val binary2 = getBlob(getColumnIndexOrThrow(PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BITMAP))
//
//                val bitmap2 = BitmapFactory.decodeByteArray(binary2,0,binary2.size)
//
//                imageView.setImageBitmap(bitmap2)
//            }
//        }

        bodyWeightForm.setText(model.bodyWeight)
        bodyFatPercentageForm.setText(model.bodyFatPercentage)
        basalMetabolicRateForm.setText(model.basalMetabolicRate)
    }

    // ピッカーから画像を選択すると、onActivityResult() が呼び出される
    // 選択した画像を指す URI が resultData パラメータに含まれ返ってくる
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            val bitmap = getBitmapFromUri(data.data)
            bitmap?:return

            imageView.setImageBitmap(bitmap)

            model.imageData = getBinaryFromBitmap(bitmap)
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

    //Binaryを取得
    private fun getBinaryFromBitmap(bitmap:Bitmap):ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}